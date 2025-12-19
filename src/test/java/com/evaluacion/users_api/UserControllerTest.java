package com.evaluacion.users_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_ok_returns201AndToken() throws Exception {

        String json = """
        {
          "name": "Juan Rodriguez",
          "email": "juan@test.cl",
          "password": "Password1",
          "phones": [
            {
              "number": "1234567",
              "citycode": "1",
              "contrycode": "57"
            }
          ]
        }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.isactive").value(true))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.last_login").exists());
    }

    @Test
    void registerUser_duplicateEmail_returns409() throws Exception {

        String json = """
        {
          "name": "Juan Rodriguez",
          "email": "duplicado@test.cl",
          "password": "Password1",
          "phones": []
        }
        """;

        // Primer registro OK
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // Segundo registro con mismo email
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El correo ya registrado"));
    }

    @Test
    void registerUser_invalidPassword_returns400() throws Exception {

        String json = """
        {
          "name": "Juan Rodriguez",
          "email": "passinvalido@test.cl",
          "password": "123",
          "phones": []
        }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Formato de contraseña inválido"));
    }

    @Test
    void registerUser_invalidJson_returns400() throws Exception {

        String json = "{ name: Juan }"; // JSON roto

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("JSON inválido"));
    }
}
