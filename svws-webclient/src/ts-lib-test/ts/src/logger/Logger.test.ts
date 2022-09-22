import { describe, test, expect, beforeEach } from "vitest";
import { LogData, Logger, List, LogLevel, LogConsumerVector } from "~/index";

describe("Logger Class", () => {
    test("is Logger instance", () => {
        const log = new Logger();
        expect(log).toBeInstanceOf(Logger);
    });
});

describe("LogConsumerVector: Working with Consumers", () => {
    let logConsumer: LogConsumerVector;
    let logData: LogData;
    const logConsumer2 = new LogConsumerVector();
    const logData2 = new LogData(LogLevel.DEBUG, 2, false, "test2");
    logConsumer2.accept(logData2);

    beforeEach(() => {
      logConsumer = new LogConsumerVector();
      logData = new LogData(LogLevel.INFO, 4, true, "test");
      logConsumer.accept(logData);
    });
    test("append", () => {
      logConsumer.append(logConsumer2);
      expect(logConsumer.getLogData().size()).toBe(2);
    });
    test("accept", () => {
        expect(logConsumer.getLogData()).toContainEqual(logData);
    });
    test("getLogData", () => {
      const ld = logConsumer.getLogData()
      expect(ld).toContainEqual(logData);
    });
    test.todo("getLogData: empty", () => {
      const logConsumer3 = new LogConsumerVector();
      expect(logConsumer3.getLogData()).toEqual(null);
    });
    test("getStrings: without parameter", () => {
      logConsumer.append(logConsumer2);
      const ld = logConsumer.getStrings()?.toArray();
      expect(ld).toContainEqual("    test");
      expect(ld).toContainEqual("  test2");
    });
    test("getStrings: with parameter", () => {
      logConsumer.append(logConsumer2);
      const ld = logConsumer.getStrings("->")?.toArray();
      expect(ld).toContainEqual("->    test");
      expect(ld).toContainEqual("->  test2");
    });
    test("getText: without parameter", () => {
      logConsumer.append(logConsumer2);
      const ld = logConsumer.getText();
      expect(ld).toBe("    test\n");
    });
    test("getText: with LogLevel parameter", () => {
      logConsumer.append(logConsumer2);
      const ld = logConsumer.getText(LogLevel.DEBUG);
      expect(ld).toBe("    test\n  test2\n");
    });
    test("getText: with LogLevel and indent parameter", () => {
      logConsumer.append(logConsumer2);
      const ld = logConsumer.getText(LogLevel.DEBUG, "->");
      expect(ld).toBe("->    test\n->  test2\n");
    });
});
describe("Logger: Working with Consumers", () => {
    let log: Logger;
    let log2: Logger;
    let logConsumer: LogConsumerVector;
    let logConsumer2: LogConsumerVector;

    beforeEach(() => {
        log = new Logger();
        logConsumer = new LogConsumerVector();
        log2 = new Logger();
        logConsumer2 = new LogConsumerVector();
    });
    test("addConsumer", () => {
        log.addConsumer(logConsumer);
        expect(log["consumer"].size()).toBe(1);
    });
    test("copyConsumer", () => {
        log.addConsumer(logConsumer);
        log2.addConsumer(logConsumer2);
        log.copyConsumer(log2);
        expect(log["consumer"].size()).toBe(2);
    });
    test("removeConsumer", () => {
        log.addConsumer(logConsumer);
        log.removeConsumer(logConsumer);
        expect(log["consumer"].size()).toBe(0);
    });
});

describe("Logger: Working with LogLevel", () => {
    let log: Logger;
    let logLevel: LogLevel;

    beforeEach(() => {
        log = new Logger();
    });
    test("Default LogLevel", () => {
        logLevel = log.getDefaultLevel();
        expect(logLevel).toBe(LogLevel.INFO);
    });
    test("Set Default LogLevel", () => {
        log.setDefaultLevel(LogLevel.DEBUG);
        expect(log.getDefaultLevel()).toBe(LogLevel.DEBUG);
    });
});

describe("Logger: Working with logs", () => {
    let log: Logger;
    let logConsumer: LogConsumerVector;

    beforeEach(() => {
        log = new Logger();
        logConsumer = new LogConsumerVector();
        log.addConsumer(logConsumer);
    });
    test("Log size", () => {
        log.logLn(0, "Eine erste Zeile im Log.");
        const data = logConsumer.getLogData() as List<LogData>;
        expect(data.size()).toBe(1);
    });
    test("Log instance", () => {
        log.logLn(0, "Eine erste Zeile im Log.");
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog).toBeInstanceOf(LogData);
    });
    test("logged text", () => {
        log.logLn(0, "Eine erste Zeile im Log.");
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog.getText()).toBe("Eine erste Zeile im Log.");
    });
    test("log: LogData", () => {
        const logdata = new LogData(LogLevel.DEBUG, 2, true, "Test Data");
        log.log(logdata);
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog.getText()).toBe("  Test Data");
        expect(firstlog.getLevel()).toBe(LogLevel.DEBUG);
        expect(firstlog.isNewLine()).toBe(true);
        //expect(firstlog.toString()).toBe("{kommt noch}");
    });
    test("log: LogLevel, indent, text", () => {
        log.log(LogLevel.DEBUG, 4, "Test Data");
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog.getText()).toBe("    Test Data");
        expect(firstlog.getLevel()).toBe(LogLevel.DEBUG);
        expect(firstlog.isNewLine()).toBe(false);
        //expect(firstlog.toString()).toBe("{kommt noch}");
    });
    test("log: LogLevel, text", () => {
        log.log(LogLevel.DEBUG, "Test Data");
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog.getText()).toBe("Test Data");
        expect(firstlog.getLevel()).toBe(LogLevel.DEBUG);
        expect(firstlog.isNewLine()).toBe(false);
        //expect(firstlog.toString()).toBe("{kommt noch}");
    });
    test("log: indent, text", () => {
        log.log(3, "Test Data");
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog.getText()).toBe("   Test Data");
        expect(firstlog.getLevel()).toBe(LogLevel.INFO);
        expect(firstlog.isNewLine()).toBe(false);
        //expect(firstlog.toString()).toBe("{kommt noch}");
    });
    test("log: text", () => {
        log.log("Test Data");
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog.getText()).toBe("Test Data");
        expect(firstlog.getLevel()).toBe(LogLevel.INFO);
        expect(firstlog.isNewLine()).toBe(false);
        //expect(firstlog.toString()).toBe("{kommt noch}");
    });
    test("logLn: LogLevel, indent, text", () => {
        log.logLn(LogLevel.DEBUG, 4, "Test Data");
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog.getText()).toBe("    Test Data");
        expect(firstlog.getLevel()).toBe(LogLevel.DEBUG);
        expect(firstlog.isNewLine()).toBe(true);
        //expect(firstlog.toString()).toBe("{kommt noch}");
    });
    test("logLn: LogLevel, text", () => {
        log.logLn(LogLevel.DEBUG, "Test Data");
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog.getText()).toBe("Test Data");
        expect(firstlog.getLevel()).toBe(LogLevel.DEBUG);
        expect(firstlog.isNewLine()).toBe(true);
        //expect(firstlog.toString()).toBe("{kommt noch}");
    });
    test("logLn: indent, text", () => {
        log.logLn(3, "Test Data");
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog.getText()).toBe("   Test Data");
        expect(firstlog.getLevel()).toBe(LogLevel.INFO);
        expect(firstlog.isNewLine()).toBe(true);
        //expect(firstlog.toString()).toBe("{kommt noch}");
    });
    test("logLn: text", () => {
        log.logLn("Test Data");
        const data = logConsumer.getLogData() as List<LogData>;
        const firstlog = data.get(0);
        expect(firstlog.getText()).toBe("Test Data");
        expect(firstlog.getLevel()).toBe(LogLevel.INFO);
        expect(firstlog.isNewLine()).toBe(true);
        //expect(firstlog.toString()).toBe("{kommt noch}");
    });
});
