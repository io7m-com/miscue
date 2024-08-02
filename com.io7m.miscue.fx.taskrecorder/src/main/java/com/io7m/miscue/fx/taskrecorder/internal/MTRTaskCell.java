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

import com.io7m.taskrecorder.core.TRTaskItemType;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import static com.io7m.miscue.fx.taskrecorder.internal.MTRErrorStrings.STRINGS;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;

public final class MTRTaskCell extends TreeCell<TRTaskItemType>
{
  private final Pane root;
  private final MTRTaskCellController controller;

  public MTRTaskCell()
  {
    try {
      final FXMLLoader loader =
        new FXMLLoader(
          MTRTaskCell.class.getResource(
            "/com/io7m/miscue/fx/taskrecorder/internal/errorTaskRecorderItem.fxml"));
      loader.setResources(STRINGS.resources());
      loader.setControllerFactory(param -> new MTRTaskCellController());
      this.root = loader.load();
      this.controller = loader.getController();
      Objects.requireNonNull(this.root, "this.root");
      Objects.requireNonNull(this.controller, "this.controller");
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  protected void updateItem(
    final TRTaskItemType item,
    final boolean empty)
  {
    super.updateItem(item, empty);

    this.setContentDisplay(GRAPHIC_ONLY);
    if (empty || item == null) {
      this.setGraphic(null);
      this.setText(null);
    } else {
      this.controller.setItem(item);
      this.setGraphic(this.root);
      this.setText(null);
    }
  }
}
