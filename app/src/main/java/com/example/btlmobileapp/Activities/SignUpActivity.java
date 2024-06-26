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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
                String phone = binding.inputPhone.getText().toString().trim();

                checkIsExistPhoneNumber(phone)
                        .thenAccept(task ->{
                            if (task){
                                signUp();
                            } else {
                                binding.txtPhone.setText("Số điện thoại đã tồn tại");
                            }
                        });

            }
        });
        binding.imageAdd.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            // cờ xin cấp quyền đọc dữ liệu URI từ intent
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.textChangeType.setOnClickListener(v -> {
            if (binding.inputPassword.getTransformationMethod() != null) {
                binding.inputPassword.setTransformationMethod(null);
                binding.textChangeType.setText("Ẩn");
            } else {
                binding.inputPassword.setTransformationMethod(new PasswordTransformationMethod());
                binding.textChangeType.setText("Hiện");
            }
        });

        binding.textChangeType2.setOnClickListener(v -> {
            if (binding.inputConfirmPassword.getTransformationMethod() != null) {
                binding.inputConfirmPassword.setTransformationMethod(null);
                binding.textChangeType2.setText("Ẩn");
            } else {
                binding.inputConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                binding.textChangeType2.setText("Hiện");
            }
        });

        binding.inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.txtName.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtName.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = binding.inputName.getText().toString().trim();
                if (name.isEmpty())
                {
                    binding.txtName.setText("Trường này còn trống");
                } else if (!checkName(name)){
                    binding.txtName.setText("Tên không hợp lệ");
                } else
                binding.txtName.setText("");
            }
        });

        binding.inputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.txtPhone.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtPhone.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = binding.inputPhone.getText().toString().trim();
                if(phone.isEmpty()){
                    binding.txtPhone.setText("Trường này còn trống");
                } else if (!checkPhone(phone)) {
                    binding.txtPhone.setText("Số điện thoại không hợp lệ");
                } else
                binding.txtPhone.setText("");
            }
        });
        binding.inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.txtEmail.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtEmail.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = binding.inputEmail.getText().toString().trim();
                if (email.isEmpty()){
                    binding.txtEmail.setText("Nên nhâp Email để cấp lại mật khẩu");
                } else if (!isValidEmailAddress(email)) {
                    binding.txtEmail.setText("Email không hợp lệ");
                } else
                binding.txtEmail.setText("");
            }
        });
        binding.inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.txtEmail.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtEmail.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = binding.inputPassword.getText().toString().trim();
                if(password.isEmpty()){
                    binding.txtPassword.setText("Trường này còn trống");
                } else if (!isValidPassword(password))
                {
                    binding.txtPassword.setText("Mật khẩu không hợp lệ , từ 6 kí tự trở lên");
                }else
                binding.txtPassword.setText("");
            }
        });
        binding.inputConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.txtConfirm.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtConfirm.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = binding.inputPassword.getText().toString().trim();
                String confirmPassword = binding.inputConfirmPassword.getText().toString().trim();
                if(confirmPassword.isEmpty())
                {
                    binding.txtConfirm.setText("Trường này còn trống");
                } else if (!isValidConfirmPassword(password,confirmPassword)) {
                    binding.txtConfirm.setText("Mật khẩu không trùng khớp");
                } else
                binding.txtConfirm.setText("");
            }
        });
    }
    private CompletableFuture<Boolean> checkIsExistPhoneNumber(String phone) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_PHONE, phone)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            future.complete(false);
                        } else {
                            future.complete(true);
                        }
                    } else {
                        future.completeExceptionally(task.getException()); // Xử lý lỗi
                    }
                });

        return future;
    }
    private boolean isValid(){
        String name = binding.inputName.getText().toString().trim();
        String phone = binding.inputPhone.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String confirmPassword = binding.inputConfirmPassword.getText().toString().trim();

        if(checkName(name) && checkPhone(phone) && isValidPassword(password) && isValidConfirmPassword(password,confirmPassword))
        {

            return true;
        }


        return false;
    }
    private boolean checkName(String name){
        if (name == null || name.isEmpty()) {
            return false;
        }

        // Biểu thức chính quy để kiểm tra tên
        String regex = "^[a-zA-Z\\s]+$";

        // Kiểm tra tên theo regex
        if (!name.matches(regex)) {
            return false;
        }

        return true;
    }
    private boolean checkPhone(String phone) {
        if(phone.isEmpty())
        {
            return false;
        } else if (!phone.matches("(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b")) {
            return false;
        }

        return true;
    }
    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    private boolean isValidPassword(String password){
        // Kiểm tra nếu password rỗng hoặc null
        if (password == null || password.isEmpty()) {
            return false;
        }

        // Biểu thức chính quy để kiểm tra mật khẩu
        String regex = "^[a-zA-Z0-9]{6,}$";

        // Kiểm tra mật khẩu theo regex và độ dài
        return password.matches(regex);
    }
    private boolean isValidConfirmPassword(String password, String confirmPassword){
        if(password.equals(confirmPassword)){
            return true;
        }
        return false;
    }

    private CompletableFuture<String> autoCreateIdAsync() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        CompletableFuture<String> future = new CompletableFuture<>();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
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