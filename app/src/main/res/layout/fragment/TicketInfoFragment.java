package com.example.train.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.train.R;
import com.example.train.api.ApiService;
import com.example.train.api.RetrofitClient;
import com.example.train.dto.response.TicketType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketInfoFragment extends Fragment {

    private Spinner spinnerTicketType;
    private List<TicketType> ticketTypes;
    private ProgressBar progressBar;  // Thêm ProgressBar để hiển thị khi đang tải dữ liệu

    public TicketInfoFragment() {
        // Required empty public constructor
    }

    public static TicketInfoFragment newInstance() {
        return new TicketInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticket_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerTicketType = view.findViewById(R.id.spinner_ticket_type);
        progressBar = view.findViewById(R.id.progress_bar);  // Thêm ProgressBar

        loadTicketTypes();
    }

    private void loadTicketTypes() {
        // Hiển thị ProgressBar khi đang tải dữ liệu
        progressBar.setVisibility(View.VISIBLE);

        // Tạo Retrofit client và gọi API
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<TicketType>> call = apiService.getTicketTypes();

        call.enqueue(new Callback<List<TicketType>>() {
            @Override
            public void onResponse(@NonNull Call<List<TicketType>> call, @NonNull Response<List<TicketType>> response) {
                // Ẩn ProgressBar khi đã tải xong
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ticketTypes = response.body();

                    // Tạo danh sách tên loại vé
                    List<String> ticketTypeNames = new ArrayList<>();
                    for (TicketType ticketType : ticketTypes) {
                        ticketTypeNames.add(ticketType.getTicketTypeName());  // Thêm tên loại vé vào danh sách
                    }

                    // Tạo adapter để hiển thị tên loại vé trong Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            ticketTypeNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTicketType.setAdapter(adapter);

                    // Set listener khi người dùng chọn một loại vé
                    spinnerTicketType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TicketType selectedType = ticketTypes.get(position);
                            int selectedId = selectedType.getTicketTypeId(); // Lấy ID loại vé
                            Toast.makeText(requireContext(), "Đã chọn loại vé ID: " + selectedId, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Không làm gì khi không chọn gì
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Không lấy được loại vé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TicketType>> call, @NonNull Throwable t) {
                // Ẩn ProgressBar khi gặp lỗi
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
