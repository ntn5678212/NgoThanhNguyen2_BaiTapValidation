package com.example.ngothanhnguyen_bttailop2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ngothanhnguyen_bttailop2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var submitted = false
    private val touchedFields = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupValidation()

        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    // 1. VALIDATE HỌ VÀ TÊN
    private fun validateName(): Boolean {
        val value = binding.edtName.text.toString().trim()

        val nameRegex = Regex(
            """^\p{L}[\p{L}\p{M}]*(?:[ '-]\p{L}[\p{L}\p{M}]*)*$"""
        )

        val error = when {
            value.isEmpty() ->
                "Vui lòng nhập họ và tên"

            value.length < 2 ->
                "Họ tên phải có ít nhất 2 ký tự"

            value.length > 60 ->
                "Họ tên không được quá 60 ký tự"

            !nameRegex.matches(value) ->
                "Họ tên không được chứa số hoặc ký tự đặc biệt"

            else -> null
        }

        binding.edtName.error = error
        return error == null
    }

    // 2. VALIDATE EMAIL
    private fun validateEmail(): Boolean {
        val value = binding.edtEmail.text.toString().trim()

        val error = when {
            value.isEmpty() ->
                "Vui lòng nhập Email"

            value.any { it.isWhitespace() } ->
                "Email không được chứa khoảng trắng"

            value.length > 254 ->
                "Email quá dài"

            !Patterns.EMAIL_ADDRESS.matcher(value).matches() ->
                "Email không đúng định dạng (vd: abc@gmail.com)"

            else -> null
        }

        binding.edtEmail.error = error
        return error == null
    }

    // 3. VALIDATE SỐ ĐIỆN THOẠI DI ĐỘNG
    private fun validatePhone(): Boolean {
        val value = binding.edtPhone.text.toString().trim()

        val error = when {
            value.isEmpty() ->
                "Vui lòng nhập số điện thoại"

            !value.all { it in '0'..'9' } ->
                "Số điện thoại chỉ được chứa chữ số"

            value.length != 10 ->
                "Số điện thoại phải có đúng 10 chữ số"

            !value.startsWith("0") ->
                "Số điện thoại phải bắt đầu bằng 0"

            !value.matches(Regex("^0[35789][0-9]{8}$")) ->
                "Đầu số di động không hợp lệ"

            else -> null
        }

        binding.edtPhone.error = error
        return error == null
    }

    // 4. VALIDATE MÃ SINH VIÊN
    private fun validateStudentId(): Boolean {
        val value = binding.edtStudentId.text.toString().trim()

        val error = when {
            value.isEmpty() ->
                "Vui lòng nhập mã sinh viên"

            !value.all { it in '0'..'9' } ->
                "Mã sinh viên chỉ được chứa chữ số"

            value.length < 8 ->
                "Mã sinh viên phải có ít nhất 8 chữ số"

            value.length > 15 ->
                "Mã sinh viên không được quá 15 chữ số"

            else -> null
        }

        binding.edtStudentId.error = error
        return error == null
    }

    // 5. VALIDATE LỚP
    private fun validateClass(): Boolean {
        val value = binding.edtClass.text.toString().trim()

        val classRegex = Regex(
            """^[\p{L}0-9]+(?:[_-][\p{L}0-9]+)*$"""
        )

        val error = when {
            value.isEmpty() ->
                "Vui lòng nhập lớp sinh hoạt"

            value.length < 3 ->
                "Tên lớp phải có ít nhất 3 ký tự"

            value.length > 30 ->
                "Tên lớp không được quá 30 ký tự"

            value.any { it.isWhitespace() } ->
                "Tên lớp không được chứa khoảng trắng"

            !classRegex.matches(value) ->
                "Tên lớp chỉ được chứa chữ, số, _ hoặc -"

            !value.any { it.isLetter() } ->
                "Tên lớp cần có ít nhất một chữ cái"

            else -> null
        }

        binding.edtClass.error = error
        return error == null
    }

    // 6. VALIDATE KHI NGƯỜI DÙNG NHẬP DỮ LIỆU
    private fun watch(
        editText: EditText,
        validate: () -> Boolean
    ) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                touchedFields.add(editText.id)
                validate()
            }
        }

        editText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (submitted || editText.id in touchedFields) {
                    validate()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupValidation() {
        watch(binding.edtName) { validateName() }
        watch(binding.edtEmail) { validateEmail() }
        watch(binding.edtPhone) { validatePhone() }
        watch(binding.edtStudentId) { validateStudentId() }
        watch(binding.edtClass) { validateClass() }
    }

    // 7. KIỂM TRA TẤT CẢ THÔNG TIN TRƯỚC KHI ĐĂNG KÝ
    private fun register() {
        submitted = true

        val validName = validateName()
        val validEmail = validateEmail()
        val validPhone = validatePhone()
        val validId = validateStudentId()
        val validClass = validateClass()

        if (!(validName && validEmail && validPhone &&
                    validId && validClass)
        ) {

            Toast.makeText(
                this,
                "Vui lòng kiểm tra lại thông tin!",
                Toast.LENGTH_SHORT
            ).show()

            when {
                !validName -> binding.edtName.requestFocus()
                !validEmail -> binding.edtEmail.requestFocus()
                !validPhone -> binding.edtPhone.requestFocus()
                !validId -> binding.edtStudentId.requestFocus()
                else -> binding.edtClass.requestFocus()
            }

            return
        }

        // 8. CHUYỂN DỮ LIỆU QUA DETAIL ACTIVITY
        val intent = Intent(this, DetailActivity::class.java).apply {

            putExtra(
                "NAME",
                binding.edtName.text.toString().trim()
            )

            putExtra(
                "EMAIL",
                binding.edtEmail.text.toString().trim()
            )

            putExtra(
                "PHONE",
                binding.edtPhone.text.toString().trim()
            )

            putExtra(
                "STUDENT_ID",
                binding.edtStudentId.text.toString().trim()
            )

            putExtra(
                "CLASS",
                binding.edtClass.text.toString().trim()
            )
        }

        startActivity(intent)
    }
}
