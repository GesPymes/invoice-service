package com.gespyme.infrastructure.adapters.output.autofirma;

import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.infrastructure.adapters.output.aws.S3Repository;
import java.io.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutofirmaService {

  private final S3Repository s3Repository;

  // @Value("${autofirma.certificatepath}")
  private String certificatePath =
      "C:\\Users\\sary_\\Desktop\\ESTUDIOS\\UOC\\CUARTO\\TFG\\repository\\invoice-service\\src\\main\\resources\\MOLINA_DELGADO_SARA___52005699P.p12";

  // @Value("${autofirma.filter}")
  private String filter = "52005699P";

  // @Value("${autofirma.password}")
  private String password = "55155606alaska";

  // @Value("${autofirma.outputFile}")
  private String outputFilePath =
      "C:\\Users\\sary_\\Desktop\\ESTUDIOS\\UOC\\CUARTO\\TFG\\repository\\invoice-service\\target\\generated-sources\\";

  // @Value("${autofirma.inputFile}")
  private String inputFilePath =
      "C:\\Users\\sary_\\Desktop\\ESTUDIOS\\UOC\\CUARTO\\TFG\\repository\\invoice-service\\target\\generated-sources\\";

  private static final String SING_COMMAND =
      "AutoFirma sign "
          + "-i C:\\Users\\sary_\\Desktop\\ESTUDIOS\\UOC\\CUARTO\\TFG\\repository\\invoice-service\\src\\main\\resources\\cat1-saramolina.pdf "
          + "-o C:\\Users\\sary_\\Desktop\\ESTUDIOS\\UOC\\CUARTO\\TFG\\repository\\invoice-service\\src\\main\\resources\\cat1-saramolina-signed.pdf "
          + "-store pkcs12:C:\\Users\\sary_\\Desktop\\ESTUDIOS\\UOC\\CUARTO\\TFG\\repository\\invoice-service\\src\\main\\resources\\MOLINA_DELGADO_SARA___52005699P.p12 "
          + "-filter 52005699P "
          + "-password 55155606alaska > log.log";

  public byte[] getSignedFile(String inputFileName) {
    try {
      String command = getCommand(inputFileName);
      Process process = Runtime.getRuntime().exec(new String[] {"cmd.exe", "/c", command});
      process.onExit();
      int exitCode = process.waitFor();

      File file = new File(outputFilePath + "Invoice-" + inputFileName + "-signed.pdf");

      return fileToByteArray(file);

    } catch (IOException | InterruptedException e) {
      throw new InternalServerError("Cannot sign invoice");
    }
  }

  public static byte[] fileToByteArray(File file) throws IOException {
    try (FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = fileInputStream.read(buffer)) != -1) {
        byteArrayOutputStream.write(buffer, 0, bytesRead);
      }
      return byteArrayOutputStream.toByteArray();
    }
  }

  private String getCommand(String fileName) {
    StringBuilder sb = new StringBuilder();
    sb.append("AutoFirma sign -i ")
        .append(inputFilePath)
        .append("Invoice-")
        .append(fileName)
        .append(".pdf")
        .append(" -o ")
        .append(outputFilePath)
        .append("Invoice-")
        .append(fileName)
        .append("-signed.pdf")
        .append(" -store pkcs12:")
        .append(certificatePath)
        .append(" -filter ")
        .append(filter)
        .append(" -password ")
        .append(password)
        .append("> log.log");
    return sb.toString();
  }
}
