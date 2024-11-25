package com.example.currencyconverter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity {
    EditText amt, source, des;
    TextView txtn;
    Retrofit retrofit;
    ExchangeRatesAPI exchangeRatesApi;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initializing UI elements
        btn = findViewById(R.id.convert);
        amt = findViewById(R.id.amt);
        txtn = findViewById(R.id.total);
        source = findViewById(R.id.source);
        des = findViewById(R.id.des);

        // Retrofit setup for Fixer API
        retrofit = new Retrofit.Builder()
                .baseUrl("https://data.fixer.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        exchangeRatesApi = retrofit.create(ExchangeRatesAPI.class);

        // Set the onClickListener for the button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performConversion();
            }
        });
    }

    void performConversion() {
        if (TextUtils.isEmpty(amt.getText().toString().trim()) ||
                TextUtils.isEmpty(source.getText().toString().trim()) ||
                TextUtils.isEmpty(des.getText().toString().trim())) {
            Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amt.getText().toString().trim());
        String sourceCurrency = source.getText().toString().trim().toUpperCase();
        String targetCurrency = des.getText().toString().trim().toUpperCase();

        // API Call for exchange rates
        exchangeRatesApi.getExchangeRates("f3b7ad6f48d30a66654e42ccdd6221bc","EUR").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject body = response.body();
                    JsonObject rates = body.getAsJsonObject("rates");

                    // Print response for debugging
                    Log.d("CurrencyConverter", "Response: " + body.toString());

                    if (rates != null) {
                        if (sourceCurrency.equals("EUR")) {
                            // Direct conversion if EUR is the base currency
                            if (rates.has(targetCurrency)) {
                                double rate = rates.get(targetCurrency).getAsDouble();
                                double convertedAmount = amount * rate;
                                txtn.setText(String.format("%.2f %s", convertedAmount, targetCurrency));
                            } else {
                                Toast.makeText(MainActivity.this, "Invalid target currency", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If the source currency isn't EUR, do a two-step conversion
                            if (rates.has(sourceCurrency) && rates.has(targetCurrency)) {
                                double sourceRate = rates.get(sourceCurrency).getAsDouble();
                                double targetRate = rates.get(targetCurrency).getAsDouble();
                                double rate = targetRate / sourceRate;
                                double convertedAmount = amount * rate;
                                txtn.setText(String.format("%.2f %s", convertedAmount, targetCurrency));
                            } else {
                                Toast.makeText(MainActivity.this, "Invalid source or target currency", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No rates available in response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get exchange rates", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

