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


package com.io7m.miscue.fx.seltzer.internal;

import com.io7m.seltzer.api.SStructuredErrorType;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The error controller.
 */

public final class MSErrorController implements Initializable
{
  private final SStructuredErrorType<?> error;
  private final Optional<Runnable> onReportCallback;
  private final Optional<Image> iconImage;
  private final Stage stage;

  @FXML private Label errorTitle;
  @FXML private ImageView icon;
  @FXML private Button cancel;
  @FXML private Button report;
  @FXML private TextArea details;
  @FXML private TextArea exception;
  @FXML private TextArea remediation;
  @FXML private TableView<Map.Entry<String, String>> errorTable;
  @FXML private TableColumn<Map.Entry<String, String>, String> errorNameColumn;
  @FXML private TableColumn<Map.Entry<String, String>, String> errorValueColumn;
  @FXML private Parent detailsContainer;
  @FXML private Parent exceptionContainer;
  @FXML private Parent remediationContainer;
  @FXML private Parent mainContainer;

  /**
   * The error controller.
   *
   * @param inError            The error
   * @param inOnReportCallback The report callback
   * @param inIconImage        The custom icon
   * @param inStage            The stage
   */

  public MSErrorController(
    final SStructuredErrorType<?> inError,
    final Optional<Runnable> inOnReportCallback,
    final Optional<Image> inIconImage,
    final Stage inStage)
  {
    this.error =
      Objects.requireNonNull(inError, "error");
    this.onReportCallback =
      Objects.requireNonNull(inOnReportCallback, "onReportCallback");
    this.iconImage =
      Objects.requireNonNull(inIconImage, "iconImage");
    this.stage =
      Objects.requireNonNull(inStage, "stage");
  }

  @Override
  public void initialize(
    final URL url,
    final ResourceBundle resourceBundle)
  {
    this.exceptionContainer.managedProperty()
      .bind(this.exceptionContainer.visibleProperty());
    this.remediationContainer.managedProperty()
      .bind(this.remediationContainer.visibleProperty());
    this.errorTable.managedProperty()
      .bind(this.errorTable.visibleProperty());

    this.iconImage.ifPresent(image -> this.icon.setImage(image));
    this.details.setText(this.error.message());

    this.errorNameColumn.setCellValueFactory(param -> {
      return new ReadOnlyStringWrapper(param.getValue().getKey());
    });
    this.errorValueColumn.setCellValueFactory(param -> {
      return new ReadOnlyStringWrapper(param.getValue().getValue());
    });

    if (!this.error.attributes().isEmpty()) {
      this.errorTable.setItems(
        FXCollections.observableList(
          this.error.attributes()
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .toList()
        )
      );
    } else {
      this.errorTable.setVisible(false);
    }

    this.error.remediatingAction()
      .ifPresentOrElse(
        text -> this.remediation.setText(text),
        () -> this.remediationContainer.setVisible(false)
      );

    this.error.exception()
      .ifPresentOrElse(
        this::fillExceptionTrace,
        () -> this.exceptionContainer.setVisible(false)
      );

    this.report.setDisable(this.onReportCallback.isEmpty());

    Platform.runLater(() -> {
      this.cancel.requestFocus();
    });
  }

  private void fillExceptionTrace(
    final Throwable throwable)
  {
    try (var writer = new StringWriter()) {
      try (var printer = new PrintWriter(writer)) {
        throwable.printStackTrace(printer);
        printer.flush();
        this.exception.setText(writer.toString());
      }
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @FXML
  private void onReportSelected()
  {
    this.onReportCallback.ifPresent(Runnable::run);
  }

  @FXML
  private void onDismissSelected()
  {
    this.stage.close();
  }
}
