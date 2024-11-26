package com.gespyme.infrastructure.adapters.output.autofirma;

import com.gespyme.commons.exeptions.InternalServerError;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class AutofirmaService {

    private static final String SING_COMMAND = "AutoFirma sign " +
            "-i C:\\Users\\sary_\\Desktop\\ESTUDIOS\\UOC\\CUARTO\\TFG\\repository\\invoice-service\\src\\main\\resources\\cat1-saramolina.pdf " +
            "-o C:\\Users\\sary_\\Desktop\\ESTUDIOS\\UOC\\CUARTO\\TFG\\repository\\invoice-service\\src\\main\\resources\\cat1-saramolina-signed.pdf " +
            "-store pkcs12:C:\\Users\\sary_\\Desktop\\ESTUDIOS\\UOC\\CUARTO\\TFG\\repository\\invoice-service\\src\\main\\resources\\MOLINA_DELGADO_SARA___52005699P.p12 " +
            "-filter 52005699P " +
            "-password 55155606alaska > log.log";

    public void executeSignCommand(String invoiceId) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", SING_COMMAND});
            process.onExit();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            int exitCode = process.waitFor();
            //TODO
            System.out.println("\nEl proceso terminó con el código: " + exitCode);

        } catch (IOException | InterruptedException e) {
            throw new InternalServerError("Cannot sign invoice");
        }
    }
}
