package sugar.sample;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import coffee.core.servlet.JSONServlet;

public class BranchServlet extends JSONServlet {

	/* (non-Javadoc)
	 * @see coffee.core.servlet.JSONServlet#doGet(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Object doGet(HttpServletRequest request) {
		Integer id = Integer.parseInt(request.getParameter("id"));

		ArrayList<Branch> list = new ArrayList<Branch>();
		for (int i=id+1; i<id + 10; i++) {
			int currentId = i * 10;

			Branch branch = new Branch();
			branch.setId(currentId);
			branch.setParentId(id);
			branch.setLabel("Unidade " + currentId);
			branch.setHasChildren(false);

			list.add(branch);
		}

		return list;
	}

}
