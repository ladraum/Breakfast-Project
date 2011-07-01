package coffee.sugar.helper;

import java.util.Collection;

public interface ITreeItem {

	String getLabel();

	void setLabel(String label);

	Integer getParentId();

	void setParentId(Integer parentId);

	void setId(Integer id);

	Integer getId();

	void setChildren(Collection<? extends ITreeItem> children);

	Collection<? extends ITreeItem> getChildren();

	void setHasChildren(Boolean hasChildren);

	Boolean getHasChildren();

}
