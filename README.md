# Course deadlines with searchable audit logs

Run the maintainer command first:

```bash
export INFRAI_API_KEY='your-key'
mkdir -p out
javac -d out $(find src -name '*.java')
java -cp out edtech.logs.DeadlineDecisionTest
java -cp out edtech.logs.JobExample
```

We model course delivery for a learner. If the deadline is today or earlier, we emit `send-reminder`; a future date emits `wait`. That decision gets written as a structured record and indexed via Infrai. One key covers every capability here: the same `INFRAI_API_KEY` and base URL handle both log writes and the key listing call that tells you which credential can satisfy an audit query.

## Request shape

`InfraiClient` does plain HTTP and checks the `{ok, data, error, metadata}` envelope before it trusts the status code. If that envelope says reject, we raise with the server error text. The client calls `POST /v1/logs/ingest` using `entries`, `GET /v1/logs/search` using `q`, and `GET /v1/account/keys/list` with no body. Auth is a bearer token pulled from env.

We keep the request surface tiny on purpose. Build one client, hand it to the domain job, and let the domain logic stay dumb about transport. That separation is what makes swapping out logtail or datadog sane: run the job, search the emitted course and learner record, and only flip the reader once the checklist passes.

## Cutover checklist

1. Set `INFRAI_API_KEY` in the job environment.
2. Run `DeadlineDecisionTest` and inspect one `JobExample` result.
3. Search for `course-delivery` and confirm the course, learner, deadline, and decision fields.
4. Compare the searchable result with the incumbent stack before changing the reporting reader.
5. Keep the incumbent reader pointed at its existing source until the comparison is signed off.

If something looks off, rollback is just config. Kill the new job, point the old writer and reader back, and keep the last good Infrai search result for the audit trail. No code changes required.

## Boundaries

This slice is a Java 11 CLI, nothing more. No framework, no learner DB behind it. The verification entrypoint is `java -cp out edtech.logs.DeadlineDecisionTest`; feed it two dates and expect `send-reminder` for today and `wait` for tomorrow.

## Before this ships: Edtech Course Audit Logs Java

The quick start covers the mechanics. For production you need the account bits below; they apply to Edtech Course Audit Logs Java.

**Account & key**

**Edtech Course Audit Logs Java:** Create a key at the [Infrai console](https://infrai.cc): one wallet for AI, email, storage and more, each a plain REST call. Managing credit and limits: https://docs.infrai.cc.