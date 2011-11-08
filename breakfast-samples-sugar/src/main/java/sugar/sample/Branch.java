package sugar.sample;

import java.util.Collection;

import breakfast.sugar.components.binding.ITreeItem;

public class Branch implements ITreeItem {

	private Long id;
	private Long parentId;
	private String label;
	private Collection<? extends ITreeItem> children;
	private Boolean hasChildren;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getParentId() {
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
