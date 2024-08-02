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


package com.io7m.miscue.tests;

import com.io7m.miscue.fx.seltzer.MSErrorDialogType;
import com.io7m.miscue.fx.seltzer.MSErrorDialogs;
import com.io7m.seltzer.api.SStructuredError;
import com.io7m.xoanon.commander.api.XCRobotType;
import com.io7m.xoanon.extension.XoExtension;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(XoExtension.class)
public final class MSErrorDialogsTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(MSErrorDialogsTest.class);

  /**
   * Error dialogs show the right fields.
   *
   * @param robot The robot
   *
   * @throws Exception On errors
   */

  @Test
  public void testErrorMinimal(
    final XCRobotType robot)
    throws Exception
  {
    final var dialog = new AtomicReference<MSErrorDialogType>();
    robot.execute(() -> {
      final var error =
        SStructuredError.builder("error-code", "A problem occurred.")
          .build();

      dialog.set(MSErrorDialogs.builder(error).build());
    });

    robot.execute(() -> dialog.get().show());
    robot.waitForFrames(600);

    final var stage =
      robot.evaluate(() -> dialog.get().stage());

    final var attributeTable =
      robot.findWithId(stage, "errorTable");
    final var exceptionContainer =
      robot.findWithId(stage, "exceptionContainer");
    final var remediationContainer =
      robot.findWithId(stage, "remediationContainer");
    final var report =
      robot.findWithId(stage, "report");

    assertFalse(attributeTable.isVisible());
    assertFalse(exceptionContainer.isVisible());
    assertFalse(remediationContainer.isVisible());
    assertTrue(report.isDisabled());

    robot.execute(() -> dialog.get().close());
  }

  /**
   * Error dialogs show the right fields.
   *
   * @param robot The robot
   *
   * @throws Exception On errors
   */

  @Test
  public void testErrorWithReporting(
    final XCRobotType robot)
    throws Exception
  {
    final var called = new AtomicBoolean(false);
    final var dialog = new AtomicReference<MSErrorDialogType>();
    robot.execute(() -> {
      final var error =
        SStructuredError.builder("error-code", "A problem occurred.")
          .build();

      dialog.set(
        MSErrorDialogs.builder(error)
          .setErrorReportCallback(() -> {
            LOG.debug("Clicked!");
            called.set(true);
          })
          .build()
      );
    });

    robot.execute(() -> dialog.get().show());
    robot.waitForFrames(600);

    final var stage =
      robot.evaluate(() -> dialog.get().stage());

    final var attributeTable =
      robot.findWithId(stage, "errorTable");
    final var exceptionContainer =
      robot.findWithId(stage, "exceptionContainer");
    final var remediationContainer =
      robot.findWithId(stage, "remediationContainer");
    final var report =
      robot.findWithId(stage, "report");

    assertFalse(attributeTable.isVisible());
    assertFalse(exceptionContainer.isVisible());
    assertFalse(remediationContainer.isVisible());
    assertFalse(report.isDisabled());

    robot.pointAt(report);
    robot.waitForFrames(1);
    robot.click(report);
    robot.waitForFrames(1);
    robot.execute(() -> dialog.get().close());

    assertTrue(called.get());
  }

  /**
   * Error dialogs show the right fields.
   *
   * @param robot The robot
   *
   * @throws Exception On errors
   */

  @Test
  public void testErrorWithException(
    final XCRobotType robot)
    throws Exception
  {
    final var dialog = new AtomicReference<MSErrorDialogType>();
    robot.execute(() -> {
      final var error =
        SStructuredError.builder("error-code", "A problem occurred.")
          .withException(new IOException("Printer out of paper."))
          .build();

      dialog.set(MSErrorDialogs.builder(error).build());
    });

    robot.execute(() -> dialog.get().show());
    robot.waitForFrames(600);

    final var stage =
      robot.evaluate(() -> dialog.get().stage());

    final var attributeTable =
      robot.findWithId(stage, "errorTable");
    final var exceptionContainer =
      robot.findWithId(stage, "exceptionContainer");
    final var exceptionField =
      (TextArea) robot.findWithId(stage, "exception");
    final var remediationContainer =
      robot.findWithId(stage, "remediationContainer");
    final var report =
      robot.findWithId(stage, "report");

    assertFalse(attributeTable.isVisible());
    assertTrue(exceptionContainer.isVisible());
    assertFalse(remediationContainer.isVisible());
    assertTrue(report.isDisabled());
    assertTrue(exceptionField.getText().contains("Printer out of paper."));

    robot.execute(() -> dialog.get().close());
  }

  /**
   * Error dialogs show the right fields.
   *
   * @param robot The robot
   *
   * @throws Exception On errors
   */

  @Test
  public void testErrorWithRemediation(
    final XCRobotType robot)
    throws Exception
  {
    final var dialog = new AtomicReference<MSErrorDialogType>();
    robot.execute(() -> {
      final var error =
        SStructuredError.builder("error-code", "A problem occurred.")
          .withRemediatingAction("Try turning it off and leaving it off.")
          .build();

      dialog.set(MSErrorDialogs.builder(error).build());
    });

    robot.execute(() -> dialog.get().show());
    robot.waitForFrames(600);

    final var stage =
      robot.evaluate(() -> dialog.get().stage());

    final var attributeTable =
      robot.findWithId(stage, "errorTable");
    final var exceptionContainer =
      robot.findWithId(stage, "exceptionContainer");
    final var exceptionField =
      (TextArea) robot.findWithId(stage, "exception");
    final var remediationField =
      (TextArea) robot.findWithId(stage, "remediation");
    final var remediationContainer =
      robot.findWithId(stage, "remediationContainer");
    final var report =
      robot.findWithId(stage, "report");

    assertFalse(attributeTable.isVisible());
    assertFalse(exceptionContainer.isVisible());
    assertTrue(remediationContainer.isVisible());
    assertTrue(report.isDisabled());
    assertTrue(remediationField.getText().contains(
      "Try turning it off and leaving it off."));

    robot.execute(() -> dialog.get().close());
  }

  /**
   * Error dialogs show the right fields.
   *
   * @param robot The robot
   *
   * @throws Exception On errors
   */

  @Test
  public void testErrorAttributes(
    final XCRobotType robot)
    throws Exception
  {
    final var dialog = new AtomicReference<MSErrorDialogType>();
    robot.execute(() -> {
      final var error =
        SStructuredError.builder("error-code", "A problem occurred.")
          .withAttribute("Attribute 0", "Value 0")
          .withAttribute("Attribute 1", "Value 1")
          .withAttribute("Attribute 2", "Value 2")
          .build();

      dialog.set(MSErrorDialogs.builder(error).build());
    });

    robot.execute(() -> dialog.get().show());
    robot.waitForFrames(600);

    final var stage =
      robot.evaluate(() -> dialog.get().stage());

    final var attributeTable =
      robot.findWithId(stage, "errorTable");
    final var exceptionContainer =
      robot.findWithId(stage, "exceptionContainer");
    final var remediationContainer =
      robot.findWithId(stage, "remediationContainer");
    final var report =
      robot.findWithId(stage, "report");

    assertTrue(attributeTable.isVisible());
    assertFalse(exceptionContainer.isVisible());
    assertFalse(remediationContainer.isVisible());
    assertTrue(report.isDisabled());

    robot.execute(() -> dialog.get().close());
  }

  /**
   * Error dialogs show the right fields.
   *
   * @param robot The robot
   *
   * @throws Exception On errors
   */

  @Test
  public void testErrorCustomCSSIcon(
    final XCRobotType robot)
    throws Exception
  {
    final var dialog = new AtomicReference<MSErrorDialogType>();

    final var image =
      robot.evaluate(() -> {
        return new Image(
          MSErrorDialogsTest.class.getResource(
            "/com/io7m/miscue/tests/face.png")
            .toString(),
          true
        );
      });

    robot.execute(() -> {
      try {
        final var error =
          SStructuredError.builder("error-code", "A problem occurred.")
            .build();

        dialog.set(
          MSErrorDialogs.builder(error)
            .setIcon(image)
            .setCSS(
              MSErrorDialogsTest.class.getResource(
                "/com/io7m/miscue/tests/psychedelic.css")
                .toURI()
            )
            .build()
        );
      } catch (final URISyntaxException e) {
        throw new RuntimeException(e);
      }
    });

    robot.execute(() -> dialog.get().show());
    robot.waitForFrames(600);

    final var stage =
      robot.evaluate(() -> dialog.get().stage());

    final var attributeTable =
      robot.findWithId(stage, "errorTable");
    final var exceptionContainer =
      robot.findWithId(stage, "exceptionContainer");
    final var remediationContainer =
      robot.findWithId(stage, "remediationContainer");
    final var report =
      robot.findWithId(stage, "report");

    assertFalse(attributeTable.isVisible());
    assertFalse(exceptionContainer.isVisible());
    assertFalse(remediationContainer.isVisible());
    assertTrue(report.isDisabled());

    robot.execute(() -> dialog.get().close());
  }

  /**
   * Error dialogs show the right fields.
   *
   * @param robot The robot
   *
   * @throws Exception On errors
   */

  @Test
  public void testErrorMinimalDismiss(
    final XCRobotType robot)
    throws Exception
  {
    final var dialog = new AtomicReference<MSErrorDialogType>();
    robot.execute(() -> {
      final var error =
        SStructuredError.builder("error-code", "A problem occurred.")
          .build();

      dialog.set(MSErrorDialogs.builder(error).build());
    });

    robot.execute(() -> dialog.get().show());
    robot.waitForFrames(600);

    final var stage =
      robot.evaluate(() -> dialog.get().stage());

    final var attributeTable =
      robot.findWithId(stage, "errorTable");
    final var exceptionContainer =
      robot.findWithId(stage, "exceptionContainer");
    final var remediationContainer =
      robot.findWithId(stage, "remediationContainer");
    final var report =
      robot.findWithId(stage, "report");
    final var cancel =
      robot.findWithId(stage, "cancel");

    assertFalse(attributeTable.isVisible());
    assertFalse(exceptionContainer.isVisible());
    assertFalse(remediationContainer.isVisible());
    assertTrue(report.isDisabled());

    robot.pointAt(cancel);
    robot.waitForFrames(1);
    robot.click(cancel);
    robot.waitForFrames(1);

    assertFalse(dialog.get().stage().isShowing());
  }

  /**
   * Error dialogs have the right modality.
   *
   * @param robot The robot
   *
   * @throws Exception On errors
   */

  @Test
  public void testErrorModality(
    final XCRobotType robot)
    throws Exception
  {
    final var dialog = new AtomicReference<MSErrorDialogType>();
    robot.execute(() -> {
      final var error =
        SStructuredError.builder("error-code", "A problem occurred.")
          .build();

      dialog.set(
        MSErrorDialogs.builder(error)
          .setModality(Modality.APPLICATION_MODAL)
          .build()
      );
    });

    robot.execute(() -> dialog.get().show());
    robot.waitForFrames(600);

    final var stage =
      robot.evaluate(() -> dialog.get().stage());

    final var attributeTable =
      robot.findWithId(stage, "errorTable");
    final var exceptionContainer =
      robot.findWithId(stage, "exceptionContainer");
    final var remediationContainer =
      robot.findWithId(stage, "remediationContainer");
    final var report =
      robot.findWithId(stage, "report");

    assertFalse(attributeTable.isVisible());
    assertFalse(exceptionContainer.isVisible());
    assertFalse(remediationContainer.isVisible());
    assertTrue(report.isDisabled());

    assertEquals(Modality.APPLICATION_MODAL, stage.getModality());
    robot.execute(() -> dialog.get().close());
  }
}
