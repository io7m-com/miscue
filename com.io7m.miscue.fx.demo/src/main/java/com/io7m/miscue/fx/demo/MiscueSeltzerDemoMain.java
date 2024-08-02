/*
 * Copyright © 2024 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */


package com.io7m.miscue.fx.demo;

import com.io7m.miscue.fx.seltzer.MSErrorDialogs;
import com.io7m.seltzer.api.SStructuredError;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * A demo.
 */


public final class MiscueSeltzerDemoMain
{
  private static final Logger LOG =
    LoggerFactory.getLogger(MiscueSeltzerDemoMain.class);

  private final Stage stage;
  private VBox root;
  private Scene scene;
  private Button show;
  private CheckBox includeAttributes;
  private CheckBox includeException;
  private CheckBox includeRemediating;
  private CheckBox allowReports;
  private CheckBox customCSS;
  private CheckBox customImage;

  private MiscueSeltzerDemoMain(
    final Stage inStage)
  {
    this.stage = Objects.requireNonNull(inStage, "stage");
  }

  /**
   * A demo.
   *
   * @param args The command-line arguments
   */

  public static void main(
    final String[] args)
  {
    Platform.startup(() -> {
      try {
        final var stage = new Stage();
        stage.setTitle("Miscue");
        stage.setMinWidth(640);
        stage.setMinHeight(400);

        final var demo = new MiscueSeltzerDemoMain(stage);
        demo.start();
      } catch (final Throwable e) {
        LOG.error("Error: ", e);
        Platform.exit();
      }
    });
  }

  private void start()
  {
    this.includeAttributes =
      new CheckBox("Include attributes");
    this.includeException =
      new CheckBox("Include exception");
    this.includeRemediating =
      new CheckBox("Include remediating action");
    this.allowReports =
      new CheckBox("Allow reporting");
    this.customCSS =
      new CheckBox("Custom CSS");
    this.customImage =
      new CheckBox("Custom Image");

    this.show = new Button("Show Error");
    this.show.setOnAction(event -> {
      var errorBuilder =
        SStructuredError.builder(
          "ERROR",
          "Something went wrong.");

      if (this.includeAttributes.isSelected()) {
        errorBuilder = errorBuilder
          .withAttribute("Attribute 0", "Value 0")
          .withAttribute("Attribute 1", "Value 1")
          .withAttribute("Attribute 2", "Value 2");
      }

      if (this.includeException.isSelected()) {
        errorBuilder =
          errorBuilder.withException(new IOException("Printer on fire."));
      }

      if (this.includeRemediating.isSelected()) {
        errorBuilder =
          errorBuilder.withRemediatingAction("Try doing something else.");
      }

      var dialogBuilder =
        MSErrorDialogs.builder(errorBuilder.build());

      if (this.allowReports.isSelected()) {
        dialogBuilder = dialogBuilder.setErrorReportCallback(() -> {
          LOG.debug("Submitting error report...");
        });
      }

      if (this.customCSS.isSelected()) {
        try {
          dialogBuilder.setCSS(
            MiscueSeltzerDemoMain.class.getResource(
                "/com/io7m/miscue/fx/demo/psychedelic.css")
              .toURI()
          );
        } catch (final URISyntaxException e) {
          throw new IllegalStateException(e);
        }
      }

      if (this.customImage.isSelected()) {
        dialogBuilder.setIcon(
          new Image(
            MiscueSeltzerDemoMain.class.getResource(
                "/com/io7m/miscue/fx/demo/face.png")
              .toString(),
            true
          )
        );
      }

      dialogBuilder.setTitle("Miscue: An error occurred.");
      final var dialog = dialogBuilder.build();
      dialog.stage().setWidth(640.0);
      dialog.stage().setHeight(480.0);
      dialog.showAndWait();
    });


    VBox.setMargin(this.includeAttributes, new Insets(0, 0, 8, 0));
    VBox.setMargin(this.includeException, new Insets(0, 0, 8, 0));
    VBox.setMargin(this.includeRemediating, new Insets(0, 0, 8, 0));
    VBox.setMargin(this.allowReports, new Insets(0, 0, 8, 0));
    VBox.setMargin(this.customCSS, new Insets(0, 0, 8, 0));
    VBox.setMargin(this.customImage, new Insets(0, 0, 8, 0));
    VBox.setMargin(this.show, new Insets(8, 0, 0, 0));

    this.root = new VBox();
    this.root.setPadding(new Insets(16.0));
    this.root.getChildren()
      .addAll(
        this.includeAttributes,
        this.includeException,
        this.includeRemediating,
        this.allowReports,
        this.customCSS,
        this.customImage,
        this.show
      );

    this.scene = new Scene(this.root);
    this.stage.setScene(this.scene);
    this.stage.show();
  }
}
