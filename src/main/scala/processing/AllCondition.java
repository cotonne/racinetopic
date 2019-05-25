package processing;

import org.htmlcleaner.*;
import org.htmlcleaner.conditional.*;

import java.util.*;

public class AllCondition implements ITagNodeCondition {
  private final ITagNodeCondition[] conditions;

  public AllCondition(ITagNodeCondition... conditions) {
    this.conditions = conditions;
  }

  @Override
  public boolean satisfy(TagNode tagNode) {
    return Arrays.stream(conditions).anyMatch(x -> x.satisfy(tagNode));
  }
}
