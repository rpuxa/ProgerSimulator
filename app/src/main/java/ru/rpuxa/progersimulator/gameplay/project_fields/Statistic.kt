package ru.rpuxa.progersimulator.gameplay.project_fields

import ru.rpuxa.progersimulator.cache.SuperSerializable

class Statistic(var downloads: Long = 0, var delete: Long = 0, var reviews: Int = 0, var reviewsCode: Reviews = Reviews(), var reviewsDesign: Reviews = Reviews()) : SuperSerializable
