/*
 * Отдельный проект: сущности и операции, неизвестные библиотеке context.
 * Публичный API использует типы из context, поэтому зависимость api.
 */

plugins {
    id("buildlogic.java-library-conventions")
}

dependencies {
    api(project(":context"))
}
