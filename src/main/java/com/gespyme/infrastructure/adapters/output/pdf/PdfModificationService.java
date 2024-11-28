package com.gespyme.infrastructure.adapters.output.pdf;

import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Component;

@Component
public class PdfModificationService {
  /*
     @Value("${pdf.source.path}")
     private String sourcePath;

     @Value("${pdf.destination.path}")
     private String destinationPath;
  */

  public byte[] createInvoicePdf(InvoiceData invoiceData) {
    try (PDDocument document = Loader.loadPDF(new File("src/main/resources/FACTURA.pdf"))) {
      PDPage page = document.getPage(0);
      try (PDPageContentStream contentStream =
          new PDPageContentStream(
              document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
        File font = new File("src/main/resources/Helvetica.ttf");
        PDType0Font helvetica = PDType0Font.load(document, font);
        contentStream.setFont(helvetica, 11);

        insertText(contentStream, 60, 500, invoiceData.getDescription());
        insertText(contentStream, 275, 500, "1");
        insertText(contentStream, 365, 500, invoiceData.getSubtotalAmount().toString());
        insertText(contentStream, 432, 500, invoiceData.getTaxRate().toString() + "%");
        insertText(contentStream, 480, 500, invoiceData.getTotalAmount().toString());
        insertText(contentStream, 470, 174, invoiceData.getTotalAmount().toString());
        insertText(contentStream, 470, 148, invoiceData.getTaxRate().toString() + "%");
        insertText(contentStream, 470, 124, invoiceData.getTotalAmount().toString());
      }
      // document.save(new File("src/main/resources/FACTURA2.pdf"));
      try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        document.save(outputStream);
        return outputStream.toByteArray();
      }
    } catch (IOException e) {
      throw new InternalServerError("Error generating invoice pdf", e);
    }
  }

  private static void insertText(PDPageContentStream contentStream, float x, float y, String text)
      throws IOException {
    contentStream.beginText();
    contentStream.newLineAtOffset(x, y);
    contentStream.showText(text);
    contentStream.endText();
  }
}
