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
import com.io7m.taskrecorder.core.TRStepFailed;
import com.io7m.taskrecorder.core.TRStepSucceeded;
import com.io7m.taskrecorder.core.TRTask;
import com.io7m.taskrecorder.core.TRTaskFailed;
import com.io7m.taskrecorder.core.TRTaskItemType;
import com.io7m.taskrecorder.core.TRTaskSucceeded;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public final class MTRTaskCellController implements Initializable
{
  private static final URI ICON_TASK_NEUTRAL;
  private static final URI ICON_TASK_SUCCEED;
  private static final URI ICON_TASK_FAIL;

  static {
    try {
      ICON_TASK_NEUTRAL =
        MTRTaskCellController.class.getResource(
          "/com/io7m/miscue/fx/taskrecorder/internal/task-24.png")
          .toURI();
      ICON_TASK_SUCCEED =
        MTRTaskCellController.class.getResource(
            "/com/io7m/miscue/fx/taskrecorder/internal/task-succeed-24.png")
          .toURI();
      ICON_TASK_FAIL =
        MTRTaskCellController.class.getResource(
            "/com/io7m/miscue/fx/taskrecorder/internal/task-fail-24.png")
          .toURI();
    } catch (final URISyntaxException e) {
      throw new IllegalStateException(e);
    }
  }

  @FXML private HBox root;
  @FXML private Label title;
  @FXML private ImageView icon;

  public MTRTaskCellController()
  {

  }

  @Override
  public void initialize(
    final URL url,
    final ResourceBundle resourceBundle)
  {
    this.title.setText("");
  }

  public void setItem(
    final TRTaskItemType taskItem)
  {
    switch (taskItem) {
      case final TRStep step -> {
        this.title.setText(step.description());

        switch (step.resolution()) {
          case final TRStepFailed failed -> {
            this.icon.setImage(
              new Image(ICON_TASK_FAIL.toString(), true));
          }

          case final TRStepSucceeded ignored -> {
            this.icon.setImage(
              new Image(ICON_TASK_SUCCEED.toString(), true));
          }
        }
      }

      case final TRTask<?> task -> {
        this.title.setText(task.description());
        switch (task.resolution()) {
          case final TRTaskFailed<?> ignored -> {
            this.icon.setImage(
              new Image(ICON_TASK_FAIL.toString(), true));
          }
          case final TRTaskSucceeded<?> ignored -> {
            this.icon.setImage(
              new Image(ICON_TASK_SUCCEED.toString(), true));
          }
        }
      }
    }
  }
}
