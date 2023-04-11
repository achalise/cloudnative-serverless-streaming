package com.aun.streamprocessor

import org.apache.kafka.streams.kstream.KStream
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka
import java.util.function.Consumer
import java.util.function.Supplier

@SpringBootApplication
class StreamprocessorApplication {
}

fun main(args: Array<String>) {
	runApplication<StreamprocessorApplication>(*args)
}

