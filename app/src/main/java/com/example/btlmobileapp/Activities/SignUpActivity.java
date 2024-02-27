package com.example.btlmobileapp.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.ActivitySignUpBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private String imageEncoded;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        listener();
    }
    private void listener(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v->{
            if (isValid()){
                signUp();
            }
        });
        binding.imageAdd.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            // cờ xin cấp quyền đọc dữ liệu URI từ intent
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }
    private boolean isValid(){



        return true;
    }
    private void signUp(){
        isLoading(true);
        // ánh xạ tới database
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // tạo đối tượng user để lưu trữ
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME,binding.inputName.getText().toString().trim());
        user.put(Constants.KEY_EMAIL,binding.inputEmail.getText().toString().trim());
        user.put(Constants.KEY_PHONE,binding.inputPhone.getText().toString().trim());
        user.put(Constants.KEY_IMAGE,imageEncoded);

        database.collection(Constants.KEY_COLLECTION_USERS).add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME,binding.inputName.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_IMAGE,imageEncoded);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e ->{
                    isLoading(false);
                    showToast(e.getMessage());
                });


    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
    // Mã hoá hình ảnh thành chuỗi
    private String encodeImage(Bitmap bitmap){
        // khai báo chiểu rộng của hình ảnh sau khi né
        int previewWidth = 150;
        // tính toán chiều cao hình ảnh cho phù hợp
        int previewHeight = bitmap.getHeight() * previewWidth/bitmap.getWidth();
        // Thực hiện co dãn hình ảnh theo kích thước mình đã khai báo
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);

        // khai báo đối tượng lưu trữ hình ảnh sau khi nén
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // thực hiện việc nén ảnh sau khi scale thành mã bit
        previewBitmap.compress(Bitmap.CompressFormat.JPEG ,50,byteArrayOutputStream );
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                // nếu chọn thành công sẽ tiếp tục thực thi , ví dụ như chọn được ảnh,
                // còn nếu tràn bộ nhớ hoặc có lỗi thì đoạn if sẽ false
                if (result.getResultCode() == RESULT_OK){

                    // Check xem người dùng có chọn được ảnh không
                    if(result.getData() != null){
                        // lấy URI của ảnh được chọn
                        Uri imageUri = result.getData().getData();
                        try {

                            // Mở một luồng đầu vào từ URI hình ảnh thu được bằng getContentResolver
                            // cho phép truy cập dữ liệu từ các content provider khác
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);

                            // Giải mã luồng đầu vào thành một đối tượng
                            // bitmap đại diện cho hình ảnh được chọn
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageAdd.setImageBitmap(bitmap);

                            // Ẩn văn bản
                            binding.textAdd.setVisibility(View.GONE);

                            // Mã hoá hình ảnh
                            imageEncoded = encodeImage(bitmap);

                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private void isLoading(boolean loading){
        if(loading){
            binding.progressbar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        } else {
            binding.progressbar.setVisibility(View.VISIBLE);
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
        }
    }

}