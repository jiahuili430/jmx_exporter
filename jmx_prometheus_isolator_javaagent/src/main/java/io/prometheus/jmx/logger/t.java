public class t{
    public static void main(String[] args) {


1) test bulk docs raises error for invalid `docs` parameter (BulkDocsTest)
   src/couchdb/test/elixir/test/bulk_docs_test.exs:111
   Assertion with == failed
   code:  assert resp.status_code == 400
   left:  500
   right: 400
   stacktrace:
     src/couchdb/test/elixir/test/bulk_docs_test.exs:150: BulkDocsTest.assert_bad_request/2
     src/couchdb/test/elixir/test/bulk_docs_test.exs:113: (test)
    }
}