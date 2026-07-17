# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

Maven WAR project targeting Java 8:

```bash
# Compile
mvn compile

# Build WAR
mvn package

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=ClassName

# Clean build
mvn clean package
```

## Purpose & Architecture

This is **ReachabilityTests** — a collection of intentionally vulnerable Java servlet test cases used to evaluate static analysis / SAST tool reachability detection (specifically Synopsys/Coverity tooling).

All servlets live in `com.synopsys.reachability` and share the same pattern:
- **Source**: `request.getParameter("evilParam")` in `doGet()`
- **Sink**: unsanitized string concatenation into a raw SQL query via JDBC `Statement.executeQuery()`

Each file represents a distinct reachability scenario:

| File | Scenario |
|------|----------|
| `SameFunction` | Source and sink in the same method — baseline true positive |
| `DifferentFunctions` | Source in `doGet`, sink in a private helper — cross-function flow |
| `DiffFunctionsCondionalParam` | Guard condition controlled by a request param — may or may not block flow |
| `DiffFunctionsCondionalParamAndAlwaysFalse` | Guard is always false — sink is reachable |
| `DiffFunctionsCondionalParamOrAlwaysTrue` | Guard is always true — sink is unreachable |
| `DiffFunctionsConstant` | Guard uses a compile-time constant expression that always evaluates true — sink unreachable (dead code) |
| `DiffFunctionsNegativeCond` | Explicit `if (true) return` guard — sink in dead code, true negative |
| `DiffFunctionsExpression` | Guard is an expression |
| `DiffFunctionsNegativeCondFunc` | Guard delegated to a helper function |
| `DiffFunctionsStringExpression` | String manipulation in the data flow path |
| `DifferentFunctionsEscaper` | `StringEscapeUtils.escapeSql()` (Apache Commons Lang) applied before the sink — sanitized |
| `DifferentFunctionsEscaperAfter` | Escaper applied after construction — still vulnerable |
| `DifferentFunctionsEscaperCSL` | Coverity Security Library escaper used |
| `DifferentFunctionsIntermediate` | Taint flows through an intermediate variable |
| `DifferentFunctionsObject` | Taint stored in an object field before reaching sink |
| `DifferentFunctionsReassign` | Taint variable is reassigned before the sink |
| `DifferentFunctionsRegexAll` | Regex validation with `^.*$` — accepts all input, still vulnerable |

## Key Dependencies

- `javax.servlet-api 3.0.1` — servlet container (provided)
- `commons-lang 2.6` — `StringEscapeUtils.escapeSql()` used as a sanitizer variant
- `com.coverity.security:coverity-escapers 1.1` — Coverity's own escaping library
- `org.testng 7.0.0` — test framework
- `log4j 1.2.12` (jar included locally) — legacy logging artifact present in repo root; note this version has known CVEs and is not used in production code