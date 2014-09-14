package media.mapping;

import org.hibernate.Session;

import java.util.List;
import java.util.ArrayList;

/**
 * Tree Node.
 */
public class TreeNode {
  private Long id;
  private String name;
  private TreeNode parent;
  private List<TreeNode> children = new ArrayList<TreeNode>();

  public TreeNode() {
  }

  public TreeNode(String name) {
    this.name = name;
  }

  public void addChild(TreeNode node) {
    children.add(node);
    node.setParent(this);
  }

  public StringBuffer print(String prefix, StringBuffer s) {
    s.append(prefix).append(name).append('\n');

    prefix = prefix + "  ";
    for (TreeNode child : children) {
      child.print(prefix, s);
    }

    return s;
  }

  public void save(Session session) {
    session.save(this);
    for (TreeNode child : children) {
      child.save(session);
    }
  }


// getters and setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<TreeNode> getChildren() {
    return children;
  }

  public void setChildren(List<TreeNode> children) {
    this.children = children;
  }

  public TreeNode getParent() {
    return parent;
  }

  public void setParent(TreeNode parent) {
    this.parent = parent;
  }

  public int getIndex() {
    if (parent == null) return 0;

    return parent.getChildren().indexOf(this);
  }

  private void setIndex(int index) {
    // not used, calculated value, see getIndex() method
  }
}
