import java.util.logging.*;

public class logger {
    private static final Logger LOGGER = Logger.getLogger(logger.class.getName());

    public static void main(String[] args) {
        LOGGER.setLevel(Level.INFO);
        LOGGER.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new ColoredFormatter());
        LOGGER.addHandler(handler);

        LOGGER.info("This is an information message");
        LOGGER.warning("This is a warning message");
        LOGGER.severe("This is a severe message");
    }

    static class ColoredFormatter extends Formatter {
        // ANSI color codes
        private static final String ANSI_RESET = "\u001B[0m";
        private static final String ANSI_RED = "\u001B[31m";
        private static final String ANSI_YELLOW = "\u001B[33m";

        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder();
            builder.append("[")
                    .append(record.getLevel().getLocalizedName())
                    .append("] ")
                    .append(formatMessage(record))
                    .append("\n");

            if (record.getLevel() == Level.WARNING) {
                return ANSI_YELLOW + builder.toString() + ANSI_RESET;
            } else if (record.getLevel() == Level.SEVERE) {
                return ANSI_RED + builder.toString() + ANSI_RESET;
            } else {
                return builder.toString();
            }
        }
    }
}
