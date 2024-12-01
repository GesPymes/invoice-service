package com.gespyme.infrastructure.adapters.output.autofirma;

import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.infrastructure.adapters.output.aws.S3Repository;
import java.io.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutofirmaService {

  private final S3Repository s3Repository;

  @Value("${autofirma.certificatepath}")
  private String certificatePath;

  @Value("${autofirma.certificatename}")
  private String certificateName;

  @Value("${autofirma.filter}")
  private String filter;

  @Value("${autofirma.password}")
  private String password;

  @Value("${autofirma.inputfilepath}")
  private String inputFilePath;

  @Value("${autofirma.outputfilepath}")
  private String outputFilePath;

  public byte[] getSignedFile(String inputFileName) {
    try {
      String absoluteInputPath = resolvePath(inputFilePath) + "\\";
      String absoluteOutputPath = resolvePath(outputFilePath) + "\\";
      String absoluteCertPath = resolvePath(certificatePath) + "\\" + certificateName + "\\";

      String command =
          getCommand(inputFileName, absoluteInputPath, absoluteOutputPath, absoluteCertPath);
      Process process = Runtime.getRuntime().exec(new String[] {"cmd.exe", "/c", command});
      process.onExit();
      int exitCode = process.waitFor();

      File file = new File(absoluteOutputPath + "Invoice-" + inputFileName + "-signed.pdf");

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

  private String getCommand(
      String fileName,
      String absolutInputPath,
      String absolutOutputPath,
      String getAbsoluteCertPath) {
    StringBuilder sb = new StringBuilder();
    sb.append("AutoFirma sign -i ")
        .append(absolutInputPath)
        .append("Invoice-")
        .append(fileName)
        .append(".pdf")
        .append(" -o ")
        .append(absolutOutputPath)
        .append("Invoice-")
        .append(fileName)
        .append("-signed.pdf")
        .append(" -store pkcs12:")
        .append(getAbsoluteCertPath)
        .append(" -filter ")
        .append(filter)
        .append(" -password ")
        .append(password);
    return sb.toString();
  }

  private String resolvePath(String relativePath) {
    return new File(relativePath).getAbsolutePath();
  }
}
