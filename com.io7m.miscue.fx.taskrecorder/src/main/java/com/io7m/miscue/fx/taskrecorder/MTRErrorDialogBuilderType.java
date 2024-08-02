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


package com.io7m.miscue.fx.taskrecorder;

import com.io7m.miscue.core.MiscueDialogBuilderType;
import javafx.scene.image.Image;
import javafx.stage.Modality;

import java.net.URI;

/**
 * The type of JavaFX error dialog builders that consume Taskrecorder results.
 */

public interface MTRErrorDialogBuilderType
  extends MiscueDialogBuilderType
{
  @Override
  MTRErrorDialogBuilderType setErrorReportCallback(
    Runnable callback);

  /**
   * Set the modality of the dialog.
   *
   * @param modality The modality
   *
   * @return this
   */

  MTRErrorDialogBuilderType setModality(
    Modality modality);

  /**
   * Set the custom CSS stylesheet to be used.
   *
   * @param css The CSS stylesheet location
   *
   * @return this
   */

  MTRErrorDialogBuilderType setCSS(
    URI css);

  /**
   * Set the custom icon to be used.
   *
   * @param image The image
   *
   * @return this
   */

  MTRErrorDialogBuilderType setIcon(
    Image image);

  @Override
  MTRErrorDialogType build();
}
