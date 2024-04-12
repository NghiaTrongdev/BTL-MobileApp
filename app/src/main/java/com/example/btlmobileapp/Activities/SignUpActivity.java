package com.example.btlmobileapp.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import kotlin.UShort;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private String imageEncoded;
    private String imageEncodedDefault;
    private String userId;
    private PreferenceManager preferenceManager;
    private int sizeofDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        listener();
        setDefaultAvata();
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

    private CompletableFuture<String> autoCreateRelationshipIdAsync(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CompletableFuture<String> future = new CompletableFuture<>();
        database.collection(Constants.KEY_RELATION_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (queryDocumentSnapshots.isEmpty()){
                            future.complete("relation001");
                        } else {
                            int size = queryDocumentSnapshots.size();
                            future.complete(String.format("relation%03d",size +1));
                        }
                    } else {
                        future.completeExceptionally(task.getException());
                    }
                });
        return future;
    }
    private CompletableFuture<String> autoCreateIdAsync() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CompletableFuture<String> future = new CompletableFuture<>();
        database.collection(Constants.KEY_COLLECTION_USERS).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                if (queryDocumentSnapshots.isEmpty()) {
                    future.complete("user001");
                } else {
                    int size = queryDocumentSnapshots.size();
                    future.complete(String.format("user%03d", size + 1));
                }
            } else {
                // Xử lý lỗi nếu có
                future.completeExceptionally(task.getException());
            }
        });
        return future;
    }
    private void signUp(){

        isLoading(true);
        // ánh xạ tới database
        autoCreateIdAsync().thenAccept(userId -> {
            FirebaseFirestore database = FirebaseFirestore.getInstance();


            // tạo đối tượng user để lưu trữ
            HashMap<String, Object> user = new HashMap<>();
            user.put(Constants.KEY_USER_ID,userId);
            user.put(Constants.KEY_NAME,binding.inputName.getText().toString().trim());
            user.put(Constants.KEY_EMAIL,binding.inputEmail.getText().toString().trim());
            user.put(Constants.KEY_PHONE,binding.inputPhone.getText().toString().trim());
            user.put(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString().trim());
            user.put(Constants.KEY_IS_ONLINE,true);
            if (imageEncoded == null){
                user.put(Constants.KEY_IMAGE,imageEncodedDefault);
            } else {
                user.put(Constants.KEY_IMAGE,imageEncoded);
            }
            user.put(Constants.KEY_BIO,"");
            user.put(Constants.KEY_CREATE_DATE,new Date());
            user.put(Constants.KEY_DATE_OF_BIRTH,new Date());
            user.put(Constants.KEY_ROLE,Constants.KEY_ROLE_MEMBER);
            user.put(Constants.KEY_LAST_LOGIN ,"");
            user.put(Constants.KEY_GENDER,"");


            database.collection(Constants.KEY_COLLECTION_USERS).add(user)
                    .addOnSuccessListener(documentReference -> {
                        preferenceManager.putBoolean(Constants.KEY_IS_ONLINE,true);
                        preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                        preferenceManager.putString(Constants.KEY_USER_NAME,binding.inputName.getText().toString().trim());
                        if (imageEncoded == null){
                            preferenceManager.putString(Constants.KEY_IMAGE,imageEncodedDefault);
                        } else {
                            preferenceManager.putString(Constants.KEY_IMAGE,imageEncoded);
                        }

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e ->{
                        isLoading(false);
                        showToast(e.getMessage());
                    });
        }).exceptionally(e ->{
            isLoading(false);
            showToast(e.getMessage());
            return null;
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
    private void setDefaultAvata(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.avata_default);
        imageEncodedDefault = encodeImage(bitmap);
    }


    private void isLoading(boolean loading) {
        if (loading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressbar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonSignUp.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.INVISIBLE);
        }
    }

}