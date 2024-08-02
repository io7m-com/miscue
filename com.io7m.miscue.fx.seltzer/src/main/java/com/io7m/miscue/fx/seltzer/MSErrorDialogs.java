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


package com.io7m.miscue.fx.seltzer;

import com.io7m.miscue.fx.seltzer.internal.MSErrorController;
import com.io7m.miscue.fx.seltzer.internal.MSErrorStrings;
import com.io7m.seltzer.api.SStructuredErrorType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A factory of JavaFX error dialogs that consume Seltzer errors.
 *
 * @see com.io7m.seltzer.api.SStructuredError
 */

public final class MSErrorDialogs
{
  private static final MSErrorStrings STRINGS;

  static {
    STRINGS = new MSErrorStrings(Locale.getDefault());
  }

  private MSErrorDialogs()
  {

  }

  /**
   * @param error The error to be displayed
   *
   * @return A new dialog builder
   */

  public static MSErrorDialogBuilderType builder(
    final SStructuredErrorType<?> error)
  {
    return new Builder(error);
  }

  private static final class Builder
    implements MSErrorDialogBuilderType
  {
    private final SStructuredErrorType<?> error;
    private URI css;
    private Optional<Runnable> errorReportCallback;
    private Modality modality;
    private Optional<Image> icon;
    private String title;

    Builder(
      final SStructuredErrorType<?> inError)
    {
      this.error =
        Objects.requireNonNull(inError, "error");
      this.errorReportCallback =
        Optional.empty();
      this.modality =
        Modality.NONE;
      this.icon =
        Optional.empty();
      this.title = "";

      try {
        this.css =
          MSErrorDialogs.class.getResource(
            "/com/io7m/miscue/fx/seltzer/internal/errorSeltzer.css"
          ).toURI();
      } catch (final URISyntaxException e) {
        throw new IllegalStateException(e);
      }
    }

    @Override
    public MSErrorDialogBuilderType setErrorReportCallback(
      final Runnable callback)
    {
      this.errorReportCallback = Optional.of(callback);
      return this;
    }

    @Override
    public MSErrorDialogBuilderType setTitle(
      final String title)
    {
      this.title = Objects.requireNonNull(title, "title");
      return this;
    }

    @Override
    public MSErrorDialogBuilderType setModality(
      final Modality newModality)
    {
      this.modality = Objects.requireNonNull(newModality, "modality");
      return this;
    }

    @Override
    public MSErrorDialogBuilderType setCSS(
      final URI newCSS)
    {
      this.css = Objects.requireNonNull(newCSS, "css");
      return this;
    }

    @Override
    public MSErrorDialogBuilderType setIcon(
      final Image image)
    {
      this.icon = Optional.of(image);
      return this;
    }

    @Override
    public MSErrorDialogType build()
    {
      try {
        final var stage =
          new Stage();

        final var layout =
          MSErrorDialogs.class.getResource(
            "/com/io7m/miscue/fx/seltzer/internal/errorSeltzer.fxml");

        Objects.requireNonNull(layout, "layout");

        final var loader =
          new FXMLLoader(layout, STRINGS.resources());
        final var controller =
          new MSErrorController(
            this.error,
            this.errorReportCallback,
            this.icon,
            stage
          );

        loader.setControllerFactory(param -> controller);

        final Pane pane = loader.load();
        pane.getStylesheets().add(this.css.toString());
        stage.setTitle(this.title);
        stage.initModality(this.modality);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setScene(new Scene(pane));
        return new Dialog(stage, controller);
      } catch (final IOException e) {
        throw new UncheckedIOException(e);
      }
    }
  }

  private static final class Dialog implements MSErrorDialogType
  {
    private final Stage stage;
    private final MSErrorController controller;

    Dialog(
      final Stage inStage,
      final MSErrorController inController)
    {
      this.stage =
        Objects.requireNonNull(inStage, "stage");
      this.controller =
        Objects.requireNonNull(inController, "controller");
    }

    @Override
    public void show()
    {
      this.stage.show();
    }

    @Override
    public void showAndWait()
    {
      this.stage.showAndWait();
    }

    @Override
    public void close()
    {
      this.stage.close();
    }

    @Override
    public Stage stage()
    {
      return this.stage;
    }
  }
}
