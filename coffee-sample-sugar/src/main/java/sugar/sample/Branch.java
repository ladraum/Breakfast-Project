package sugar.sample;

import java.util.Collection;

import coffee.sugar.helper.ITreeItem;

public class Branch implements ITreeItem {

	private Integer id;
	private Integer parentId;
	private String label;
	private Collection<? extends ITreeItem> children;
	private Boolean hasChildren;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setChildren(Collection<? extends ITreeItem> children) {
		this.children = children;
	}

	public Collection<? extends ITreeItem> getChildren() {
		return children;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public Boolean getHasChildren() {
		return hasChildren;
	}
}
