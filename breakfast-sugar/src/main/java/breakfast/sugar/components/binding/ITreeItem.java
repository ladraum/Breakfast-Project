package breakfast.sugar.components.binding;

import java.util.Collection;

public interface ITreeItem {

	String getLabel();

	void setLabel(String label);

	Long getParentId();

	void setParentId(Long parentId);

	void setId(Long id);

	Long getId();

	void setChildren(Collection<? extends ITreeItem> children);

	Collection<? extends ITreeItem> getChildren();

	void setHasChildren(Boolean hasChildren);

	Boolean getHasChildren();

}
