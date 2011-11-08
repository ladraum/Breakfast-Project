package sugar.sample;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import breakfast.coffee.servlet.JSONServlet;


@WebServlet("/services/branch")
public class BranchServlet extends JSONServlet {

	/* (non-Javadoc)
	 * @see breakfast.coffee.servlet.JSONServlet#doGet(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Object doGet(HttpServletRequest request) {
		Integer id = Integer.parseInt(request.getParameter("id"));

		ArrayList<Branch> list = new ArrayList<Branch>();
		for (int i=id+1; i<id + 10; i++) {
			int currentId = i * 10;

			Branch branch = new Branch();
			branch.setId((long)currentId);
			branch.setParentId((long)id);
			branch.setLabel("Branch " + currentId);
			branch.setHasChildren(false);

			list.add(branch);
		}

		return list;
	}

}
