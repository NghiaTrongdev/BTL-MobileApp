package com.example.btlmobileapp.Fragments;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.btlmobileapp.Models.User;
import com.example.btlmobileapp.R;
import com.example.btlmobileapp.Utilities.Constants;
import com.example.btlmobileapp.Utilities.PreferenceManager;
import com.example.btlmobileapp.databinding.ActivityFragmentInformationBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentInformation extends AppCompatActivity {

    private ActivityFragmentInformationBinding binding;
    private PreferenceManager preferenceManager;
    private DatePickerDialog datePickerDialog;
    private  int buttontype = 0;
    private String imageEncoded;
    private String date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFragmentInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        setListener();
        renderProfileInformation();
        initDatePicker();
    }

    private void setListener() {
        binding.imageBack.setOnClickListener(v -> finish());
        binding.buttonSelectDate.setOnClickListener(v->{
            datePickerDialog.show();
        });
        binding.buttonSave.setOnClickListener(v->{
            if (buttontype == 0){
                buttontype = 1 ;
                binding.editTextName.setEnabled(true);
                binding.editTexPhone.setEnabled(true);
                binding.editTextEmail.setEnabled(true);
                binding.buttonSelectDate.setEnabled(true);
                binding.radioGroupGender.setEnabled(true);
                binding.radioButtonFemale.setEnabled(true);
                binding.radioButtonMale.setEnabled(true);
                binding.radioButtonOther.setEnabled(true);
                binding.buttonSave.setText("Save");
            } else {
                buttontype = 0;
                binding.editTextName.setEnabled(false);
                binding.editTexPhone.setEnabled(false);
                binding.editTextEmail.setEnabled(false);
                binding.buttonSelectDate.setEnabled(false);
                binding.radioGroupGender.setEnabled(false);
                binding.radioButtonFemale.setEnabled(false);
                binding.radioButtonMale.setEnabled(false);
                binding.radioButtonOther.setEnabled(false);
                if (isValid()){
                    updateProfile();
                }
                binding.buttonSave.setText("Edit");
            }

        });
        binding.imageAdd.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            // cờ xin cấp quyền đọc dữ liệu URI từ intent
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    // Phương thức cập nhật giao diện với thông tin người dùng
    public void renderProfileInformation() {
        binding.textViewSelectedDate.setText("Ngày sinh đã chọn: "+getTodayDate());
        binding.imageAdd.setImageBitmap(getUserImage(preferenceManager.getString(Constants.KEY_IMAGE)));
        binding.editTextName.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.editTextName.setEnabled(false);
        binding.editTexPhone.setText(preferenceManager.getString(Constants.KEY_PHONE));
        binding.editTexPhone.setEnabled(false);
        binding.editTextEmail.setText(preferenceManager.getString(Constants.KEY_EMAIL));
        binding.editTextEmail.setEnabled(false);
        binding.textViewSelectedDate.setText(preferenceManager.getString(Constants.KEY_DATE_OF_BIRTH));

        binding.radioButtonFemale.setEnabled(false);
        binding.radioButtonMale.setEnabled(false);
        binding.radioButtonOther.setEnabled(false);

    }
    private String getTodayDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        month = month+1;
        int day = calendar.get(calendar.DAY_OF_MONTH);
        return makeString(day,month,year);
    }
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                date = makeString(dayOfMonth,month,year);
                binding.textViewSelectedDate.setText("Ngày sinh đã chọn: "+date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog= new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }
    private String makeString(int day,int month,int year){
        return getMonthFormat(month) +" " + day + " " + year;
    }
    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "Invalid month";
        }
    }

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

                            // Mã hoá hình ảnh
                            imageEncoded = encodeImage(bitmap);

                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private boolean isValid() {
        // Lấy dữ liệu từ các trường nhập liệu
        String name = binding.editTextName.getText().toString().trim();
        String phone = binding.editTexPhone.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();

        // Kiểm tra trường số điện thoại
        if (phone.isEmpty()) {
            return false;
        } else if (!phone.matches("(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b")) {
            return false;
        }

        // Kiểm tra trường tên
        if (name.isEmpty()) {
            return false;
        } else {
            // Biểu thức chính quy để kiểm tra tên
            String nameRegex = "^[a-zA-Z\\s]+$";

            // Kiểm tra tên theo regex
            if (!name.matches(nameRegex)) {
                return false;
            }
        }

        // Kiểm tra trường email
        if (email.isEmpty()) {
            showToast("Please enter email");
            return false;
        } else {
            // Biểu thức chính quy để kiểm tra email
            String emailRegex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(emailRegex);
            java.util.regex.Matcher matcher = pattern.matcher(email);

            // Kiểm tra email theo regex
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    private void updateProfile(){
        String name = binding.editTextName.getText().toString().trim();
        String phone = binding.editTexPhone.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();

        String gender;
        int selectedRadioButtonId = binding.radioGroupGender.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            gender = selectedRadioButton.getText().toString();
        } else {
            gender = "";
        }
        isLoading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        Map<String, Object> userData = new HashMap<>();
        userData.put(Constants.KEY_NAME, name);
        userData.put(Constants.KEY_PHONE, phone);
        userData.put(Constants.KEY_EMAIL, email);
        userData.put(Constants.KEY_GENDER, gender);
        userData.put(Constants.KEY_DATE_OF_BIRTH,date);
        if (imageEncoded != null){
            userData.put(Constants.KEY_IMAGE,imageEncoded);

        }

        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_USER_ID, userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            database.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(documentId)
                                    .update(userData )
                                    .addOnSuccessListener(v -> {
                                        showToast("Success");
                                        preferenceManager.putString(Constants.KEY_IMAGE,imageEncoded);
                                        preferenceManager.putString(Constants.KEY_NAME,name);
                                        preferenceManager.putString(Constants.KEY_PHONE,phone);
                                        preferenceManager.putString(Constants.KEY_EMAIL,email);
                                        preferenceManager.putString(Constants.KEY_GENDER,gender);
                                        preferenceManager.putString(Constants.KEY_DATE_OF_BIRTH,date);
                                        isLoading(false);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        showToast("fail");
                                        isLoading(false);
                                    });

                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("FailToQuery", "updatePassword: " + e.getMessage());
                });
    }
    // Phương thức hiển thị thông báo lỗi
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    private void isLoading(boolean load) {
        if (load) {
            binding.progess.setVisibility(View.VISIBLE);
            binding.buttonSave.setVisibility(View.INVISIBLE);
        } else {
            binding.progess.setVisibility(View.INVISIBLE);
            binding.buttonSave.setVisibility(View.VISIBLE);
        }
    }
}