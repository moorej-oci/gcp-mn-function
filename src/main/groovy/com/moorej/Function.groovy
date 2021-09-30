package com.moorej
import com.google.cloud.functions.*
import io.micronaut.gcp.function.GoogleFunctionInitializer

import javax.inject.*

class Function extends GoogleFunctionInitializer // <1>
        implements BackgroundFunction<PubSubMessage> { // <2>

    @Inject LoggingService loggingService // <3>

    @Override
    void accept(PubSubMessage message, Context context) {
        message.setContext(context)
        loggingService.logMessage(message)
        String textMessage = message.decode()
    }
}

class PubSubMessage {
    String data
    Map<String, String> attributes
    String messageId
    String publishTime

    String decode() {
        return new String(Base64.getDecoder().decode(data));
    }

    void setContext(Context context) {
        messageId = context.eventId()
        publishTime = context.timestamp()
    }
}

@Singleton
class LoggingService {
    static void logMessage(PubSubMessage message) {
        System.out.println("id:${message.messageId}, time:${message.publishTime}, message:${message.decode()}")
        if (message.attributes) {
            System.out.println("The message has ${message.attributes.size()} attributes")
            for (attribute in message.attributes) {
                System.out.println("  ${attribute.key}: ${attribute.value}")
            }
        }
    }
}
