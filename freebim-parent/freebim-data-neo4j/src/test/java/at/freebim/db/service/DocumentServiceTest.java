package at.freebim.db.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import at.freebim.db.configuration.Neo4jTestConfiguration;
import at.freebim.db.domain.BigBangNode;
import at.freebim.db.domain.Document;
import at.freebim.db.domain.Library;
import at.freebim.db.domain.rel.DocumentedIn;
import at.freebim.db.domain.rel.ParentOf;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jTestConfiguration.class)
@Transactional
public class DocumentServiceTest extends AbstractBaseTest {

	@Autowired
	private DocumentService documentService;

	@Autowired
	private BigBangNodeService bigBangNodeService;

	@Autowired
	private LibraryService libraryService;

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void findByBsddGuidTest() {
		Document document = new Document();
		document.setName("TEST");
		document.setBsddGuid("TESTBSDD");

		document = documentService.save(document);
		assertNotNull(document.getNodeId());

		List<Document> dTemp = documentService.findByBsddGuid("TESTBSDD");

		assertEquals(1, dTemp.size());
		assertEquals(document.getNodeId(), dTemp.get(0).getNodeId());
	}

	@Test
	@WithMockUser(username = "admin2", roles = { "EDIT", "ADMIN" })
	public void getAllRelevantTest() {
		BigBangNode bigBangNode = bigBangNodeService.getBigBangNode();

		Library library = new Library();
		library.setName("TEST");

		library = libraryService.save(library);
		assertNotNull(library.getNodeId());

		Document document = new Document();
		document.setName("TESTD");

		document = documentService.save(document);
		assertNotNull(document.getNodeId());

		ParentOf parentOf = new ParentOf();
		parentOf.setN1(bigBangNode);
		parentOf.setN2(library);

		session.save(parentOf);

		DocumentedIn documentedIn = new DocumentedIn();
		documentedIn.setN1(library);
		documentedIn.setN2(document);

		session.save(documentedIn);

		List<Document> result = documentService.getAllRelevant();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(document.getNodeId(), result.get(0).getNodeId());
	}
}
