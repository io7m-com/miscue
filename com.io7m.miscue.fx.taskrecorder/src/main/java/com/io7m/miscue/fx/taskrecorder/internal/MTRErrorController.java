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


package com.io7m.miscue.fx.taskrecorder.internal;

import com.io7m.taskrecorder.core.TRStep;
import com.io7m.taskrecorder.core.TRTask;
import com.io7m.taskrecorder.core.TRTaskItemType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The error controller.
 */

public final class MTRErrorController implements Initializable
{
  private final TRTask<?> task;
  private final Optional<Runnable> onReportCallback;
  private final Optional<Image> iconImage;
  private final Stage stage;

  @FXML private Label errorTitle;
  @FXML private ImageView icon;
  @FXML private TreeView<TRTaskItemType> taskTree;

  /**
   * The error controller.
   *
   * @param inTask             The task
   * @param inOnReportCallback The report callback
   * @param inIconImage        The custom icon
   * @param inStage            The stage
   */

  public MTRErrorController(
    final TRTask<?> inTask,
    final Optional<Runnable> inOnReportCallback,
    final Optional<Image> inIconImage,
    final Stage inStage)
  {
    this.task =
      Objects.requireNonNull(inTask, "error");
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
    this.iconImage.ifPresent(image -> this.icon.setImage(image));
    this.taskTree.setRoot(buildTree(this.task));
    this.taskTree.setCellFactory(param -> new MTRTaskCell());
    this.taskTree.setShowRoot(false);
  }

  private static TreeItem<TRTaskItemType> buildTree(
    final TRTaskItemType item)
  {
    return switch (item) {
      case final TRStep currentStep -> {
        yield new TreeItem<>(currentStep);
      }
      case final TRTask<?> currentTask -> {
        final var rItem = new TreeItem<TRTaskItemType>(currentTask);
        for (final var subItem : currentTask.items()) {
          rItem.getChildren().add(buildTree(subItem));
        }
        yield rItem;
      }
    };
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
