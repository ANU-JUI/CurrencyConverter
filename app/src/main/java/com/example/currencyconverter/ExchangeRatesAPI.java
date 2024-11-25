package com.example.currencyconverter;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExchangeRatesAPI {
  @GET("latest")
  Call<JsonObject> getExchangeRates(
          @Query("access_key") String apiKey,  // API key parameter
          @Query("base") String baseCurrency   // Base currency parameter, e.g., "EUR" if on free plan
  );
}
