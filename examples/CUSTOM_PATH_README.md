# Custom Metrics Path Configuration

This document explains how to configure a custom metrics endpoint path in the JMX Exporter.

## Overview

By default, JMX Exporter exposes metrics at `/metrics`. You can customize this path using the `httpServer.path` configuration in your YAML file.

## Quick Start

### Example Configuration

See [`custom_path_config.yml`](custom_path_config.yml) for a complete example:

```yaml
---
httpServer:
  path: /custom/metrics

rules:
  - pattern: ".*"
```

### Usage with Java Agent

```bash
# Start your application with custom metrics path
java -javaagent:jmx_prometheus_javaagent.jar=8080:custom_path_config.yml -jar your-app.jar

# Access metrics at the custom path
curl http://localhost:8080/custom/metrics
```

### Usage with Standalone

```bash
# Start standalone exporter with custom metrics path
java -jar jmx_prometheus_standalone.jar 8080 custom_path_config.yml

# Access metrics at the custom path
curl http://localhost:8080/custom/metrics
```

## Configuration Options

### Basic Path Configuration

```yaml
httpServer:
  path: /my-metrics
```

### Path Without Leading Slash (Auto-corrected)

```yaml
httpServer:
  path: my-metrics # Will be converted to /my-metrics
```

### Combined with Other Settings

```yaml
httpServer:
  path: /prometheus/jmx
  threads:
    minimum: 1
    maximum: 10
    keepAliveTime: 120
  authentication:
    type: basic
    username: admin
    password: secret
```

## Default Behavior

If `httpServer.path` is not specified, the default path `/metrics` is used:

```yaml
# This configuration uses the default /metrics path
rules:
  - pattern: ".*"
```

## Prometheus Scrape Configuration

When using a custom path, update your Prometheus scrape configuration:

```yaml
scrape_configs:
  - job_name: "jmx-exporter"
    metrics_path: "/custom/metrics" # Match your YAML configuration
    static_configs:
      - targets: ["localhost:8080"]
```

## Validation

The path configuration:

- Must not be blank/empty
- Will be automatically prefixed with `/` if not present
- Can contain multiple path segments (e.g., `/app/monitoring/metrics`)

## Use Cases

1. **Multi-tenant environments**: Different paths for different applications
2. **Reverse proxy routing**: Match your infrastructure's routing rules
3. **API versioning**: Include version in path (e.g., `/v1/metrics`)
4. **Namespace organization**: Group metrics by service (e.g., `/services/app1/metrics`)

## Testing

To verify your configuration:

```bash
# Start the exporter
java -javaagent:jmx_prometheus_javaagent.jar=8080:custom_path_config.yml -jar app.jar

# Test the custom endpoint
curl -v http://localhost:8080/custom/metrics

# Verify the default path returns 404
curl -v http://localhost:8080/metrics  # Should return 404
```

## See Also

- [Main README](../README.md)
- [Standalone Sample Config](standalone_sample_config.yml)
- Full documentation in `docs/content/en/configuration/custom-metrics-path.md`
