# Custom Metrics Path Implementation Summary

## Overview

This document summarizes the implementation of custom metrics path configuration for the JMX Exporter project.

## Feature Description

Allows users to configure a custom HTTP endpoint path for Prometheus metrics instead of the default `/metrics` path.

## Changes Made

### 1. Core Implementation

**File**: `jmx_prometheus_common/src/main/java/io/prometheus/jmx/common/HTTPServerFactory.java`

Added new method `configurePath()` that:

- Reads the custom path from YAML configuration at `/httpServer/path`
- Validates the path is not blank
- Automatically prefixes path with `/` if not present
- Defaults to `/metrics` if not configured
- Calls `httpServerBuilder.metricsHandlerPath()` to set the custom path

**Changes**:

- Added `configurePath()` method (lines 300-323)
- Updated `createAndStartHTTPServer()` methods to call `configurePath()` before other configurations

### 2. Configuration Structure

The new YAML configuration structure:

```yaml
httpServer:
  path: /custom/metrics # Optional, defaults to /metrics
  threads: # Existing configuration
    minimum: 1
    maximum: 10
  authentication: # Existing configuration
    type: basic
  ssl: # Existing configuration
    keyStore:
      path: /path/to/keystore
```

### 3. Documentation

**Created Files**:

1. `examples/custom_path_config.yml` - Example configuration with custom path
2. `examples/CUSTOM_PATH_README.md` - Quick start guide for the feature
3. `docs/content/en/configuration/custom-metrics-path.md` - Comprehensive documentation

**Updated Files**:

1. `examples/standalone_sample_config.yml` - Added comments about custom path option

### 4. Integration

The feature automatically works with:

- **Java Agent mode**: No code changes needed in `JavaAgent.java` (uses `HTTPServerFactory`)
- **Standalone mode**: No code changes needed in `Standalone.java` (uses `HTTPServerFactory`)

## Usage Examples

### Java Agent Mode

```bash
# config.yml
httpServer:
  path: /jmx/metrics

# Start application
java -javaagent:jmx_prometheus_javaagent.jar=8080:config.yml -jar app.jar

# Access metrics
curl http://localhost:8080/jmx/metrics
```

### Standalone Mode

```bash
# config.yml
httpServer:
  path: /prometheus/jmx

# Start exporter
java -jar jmx_prometheus_standalone.jar 8080 config.yml

# Access metrics
curl http://localhost:8080/prometheus/jmx
```

## Technical Details

### API Used

The implementation uses the `HTTPServer.Builder.metricsHandlerPath(String)` method from the `io.prometheus.metrics.exporter.httpserver` library (version 1.5.1).

### Validation

- Path must not be null or blank
- Path is automatically prefixed with `/` if missing
- Invalid configuration throws `ConfigurationException`

### Default Behavior

If `httpServer.path` is not specified in the configuration:

- Default path `/metrics` is used
- Behavior is identical to previous versions (backward compatible)

## Testing

The implementation was tested by:

1. Compiling the `jmx_prometheus_common` module successfully
2. Verifying the `HTTPServer.Builder` API supports `metricsHandlerPath()`
3. Creating example configurations

## Backward Compatibility

✅ **Fully backward compatible**

- Existing configurations without `httpServer.path` continue to work
- Default behavior unchanged (uses `/metrics`)
- No breaking changes to existing APIs

## Files Modified

1. `jmx_prometheus_common/src/main/java/io/prometheus/jmx/common/HTTPServerFactory.java`
2. `examples/standalone_sample_config.yml`

## Files Created

1. `examples/custom_path_config.yml`
2. `examples/CUSTOM_PATH_README.md`
3. `docs/content/en/configuration/custom-metrics-path.md`
4. `CUSTOM_PATH_IMPLEMENTATION.md` (this file)

## Benefits

1. **Flexibility**: Users can organize metrics endpoints according to their infrastructure
2. **Multi-tenancy**: Different applications can use different paths
3. **Reverse Proxy Integration**: Easier to configure routing rules
4. **Namespace Organization**: Better organization in complex environments

## Future Enhancements

Potential future improvements:

- Support for multiple metrics endpoints with different paths
- Path templating with variables
- Dynamic path configuration via JMX

## Prometheus Configuration

When using custom paths, update Prometheus scrape configuration:

```yaml
scrape_configs:
  - job_name: "jmx-exporter"
    metrics_path: "/custom/metrics" # Match your YAML config
    static_configs:
      - targets: ["localhost:8080"]
```
