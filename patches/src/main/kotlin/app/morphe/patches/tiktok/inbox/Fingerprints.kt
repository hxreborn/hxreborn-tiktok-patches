/*
 * SPDX-FileCopyrightText: 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.tiktok.inbox

import app.morphe.patcher.Fingerprint

internal object InboxSkylightWidgetEnableFingerprint : Fingerprint(
    definingClass = "/InboxSkylightWidgetV2Injector;",
    name = "enable",
    returnType = "Z",
    parameters = emptyList(),
)

private const val NOTIFICATION_WIDGET_CONTAINER_DESCRIPTOR =
    "/NotificationWidgetContainer;"

internal object ExpandNotificationListFingerprint : Fingerprint(
    definingClass = NOTIFICATION_WIDGET_CONTAINER_DESCRIPTOR,
    returnType = "V",
    strings = listOf("expandNotification()"),
)

internal object NotificationWidgetContainerOnCreateFingerprint : Fingerprint(
    definingClass = NOTIFICATION_WIDGET_CONTAINER_DESCRIPTOR,
    name = "onCreate",
    returnType = "V",
    parameters = emptyList(),
)

internal object FollowerWidgetContainerConstructorFingerprint : Fingerprint(
    definingClass = "/FollowerWidgetContainer;",
    name = "<init>",
    returnType = "V",
    parameters = emptyList(),
)

internal object NotificationWidgetContainerConstructorFingerprint : Fingerprint(
    definingClass = NOTIFICATION_WIDGET_CONTAINER_DESCRIPTOR,
    name = "<init>",
    returnType = "V",
    parameters = emptyList(),
)
