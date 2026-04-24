---
title: Custom Metrics Path
weight: 5
---

## Overview

By default, the JMX Exporter exposes Prometheus metrics at the `/metrics` endpoint. You can customize this path using the YAML configuration file.

## Configuration

Add the `httpServer.path` configuration to your YAML file:

```yaml
---
httpServer:
  path: /custom/metrics

# Other configurations...
rules:
  - pattern: ".*"
```

## Behavior

- **Default Path**: If not specified, metrics are exposed at `/metrics`
- **Auto-prefix**: If the path doesn't start with `/`, it will be automatically prefixed
- **Validation**: The path must not be blank

## Examples

### Basic Custom Path

```yaml
---
httpServer:
  path: /prometheus/metrics

rules:
  - pattern: ".*"
```

Access metrics at: `http://localhost:8080/prometheus/metrics`

### Path Without Leading Slash

```yaml
---
httpServer:
  path: custom-metrics

rules:
  - pattern: ".*"
```

The path will be automatically converted to `/custom-metrics`

### Combined with Other HTTP Server Settings

```yaml
---
httpServer:
  path: /jmx/metrics
  threads:
    minimum: 2
    maximum: 20
    keepAliveTime: 120
  authentication:
    type: basic
    username: admin
    password: secret

rules:
  - pattern: ".*"
```

## Usage

### Java Agent Mode

```bash
java -javaagent:jmx_prometheus_javaagent.jar=8080:config.yml -jar your-app.jar
```

Then access metrics at: `http://localhost:8080/custom/metrics` (if configured)

### Standalone Mode

```bash
java -jar jmx_prometheus_standalone.jar 8080 config.yml
```

Then access metrics at: `http://localhost:8080/custom/metrics` (if configured)

## Use Cases

1. **Namespace Separation**: Use different paths for different applications
   - `/app1/metrics`
   - `/app2/metrics`

2. **Reverse Proxy Integration**: Match your proxy routing rules
   - `/monitoring/prometheus`
   - `/observability/metrics`

3. **Security Through Obscurity**: Use non-standard paths (not recommended as primary security)
   - `/internal/telemetry`

## Notes

- The custom path only affects the metrics endpoint
- Health check endpoints (if any) are not affected
- Ensure your Prometheus scrape configuration matches the custom path:

```yaml
scrape_configs:
  - job_name: "jmx-exporter"
    metrics_path: "/custom/metrics" # Match your configuration
    static_configs:
      - targets: ["localhost:8080"]
```
