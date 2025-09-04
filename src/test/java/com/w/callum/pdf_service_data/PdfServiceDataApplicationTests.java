package com.w.callum.pdf_service_data;

import com.w.callum.pdf_service_data.controller.BasicRoutes;
import com.w.callum.pdf_service_data.model.request.PostMetaRequest;
import com.w.callum.pdf_service_data.model.response.PostMetaResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PdfServiceDataApplicationTests {

    @Test
    void metaDuplicatesCheck() {
        BasicRoutes routes = new BasicRoutes();

        PostMetaRequest req = new PostMetaRequest();
        req.setBase64("JVBERi0xLjcKJYGBgYEKCjQgMCBvYmoKPDwKL0ZpbHRlciAvRmxhdGVEZWNvZGUKL0xlbmd0aCAxMAo+PgpzdHJlYW0KSIkCCDAAAAAAAQplbmRzdHJlYW0KZW5kb2JqCgo3IDAgb2JqCjw8Ci9GaWx0ZXIgL0ZsYXRlRGVjb2RlCi9UeXBlIC9PYmpTdG0KL04gNQovRmlyc3QgMjYKL0xlbmd0aCAzMDIKPj4Kc3RyZWFtCnic1VJNa4QwFLznV7xje8rbGKMWEVo/LmVB7J669BA0LEIxi0bY/vu+qP2Cll5bZIzJm/FNktkBgoAQIYBYQghhEIMCFceQpowfXs4GeK1PZmL8vu8mOBIHoSGOfz8xntt5cCBYlrEPRa6dfrYntkph58lvjHq03dyaEdKqrCrECBGVJChEUdCYExKCoDnVREzfhEhuoLUoQAxuqVatUNGq8fWFG276kkbiKs8pVq6M1/l7X9+rXP8hfvOTZIzvbVdoZ+CquBEoQkxQUikM5OM1HcdotLP/d3OL/94OP+5wu8XcDs4MbgK53C3p7PnOXigfSI/aCYgS4fOxN12vv600ZrLz2FI+tli0D8YRjddFBfxgLo5Y1I431nk3+DmPPlojGfiarb/n6hVfjM06CmVuZHN0cmVhbQplbmRvYmoKCjggMCBvYmoKPDwKL1NpemUgOQovUm9vdCAyIDAgUgovSW5mbyAzIDAgUgovRmlsdGVyIC9GbGF0ZURlY29kZQovVHlwZSAvWFJlZgovTGVuZ3RoIDQxCi9XIFsgMSAyIDIgXQovSW5kZXggWyAwIDkgXQo+PgpzdHJlYW0KeJwlxzEKACAQBLGZU7D1jf6/tj1ZbAIBuosFwVCyf0eYckAvPF+9A6IKZW5kc3RyZWFtCmVuZG9iagoKc3RhcnR4cmVmCjUwMgolJUVPRg==");

        Mono<Object> metaData = routes.getMetaData(req);
        Object o = metaData.block();
        Assertions.assertInstanceOf(ResponseEntity.class, o);
        ResponseEntity<?> res = (ResponseEntity<?>) o;
        Assertions.assertTrue(res.getStatusCode().is2xxSuccessful());

        PostMetaResponse metaResponse = (PostMetaResponse) res.getBody();
        Assertions.assertNotNull(metaResponse);
        Assertions.assertEquals(1, metaResponse.getNoOfPages());
        Assertions.assertEquals(1, metaResponse.getImages().size());
        System.out.println(metaResponse.getNoOfPages());
    }
}
