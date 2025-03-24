package edivad.extrastorage.autocrafting.advancedautocrafter;

import com.refinedmods.refinedstorage.api.autocrafting.Pattern;
import com.refinedmods.refinedstorage.api.autocrafting.task.Task;
import com.refinedmods.refinedstorage.api.network.autocrafting.ParentContainer;
import com.refinedmods.refinedstorage.api.network.autocrafting.PatternProvider;

class AutocrafterParentContainer implements ParentContainer {

  private final AdvancedAutocrafterBlockEntity blockEntity;

  AutocrafterParentContainer(final AdvancedAutocrafterBlockEntity blockEntity) {
    this.blockEntity = blockEntity;
  }

  @Override
  public void add(final PatternProvider provider, final Pattern pattern, final int priority) {
    // no op
  }

  @Override
  public void remove(final PatternProvider provider, final Pattern pattern) {
    // no op
  }

  @Override
  public void update(final Pattern pattern, final int priority) {
    // no op
  }

  @Override
  public void taskAdded(final PatternProvider provider, final Task task) {
    blockEntity.setChanged();
  }

  @Override
  public void taskRemoved(final Task task) {
    blockEntity.setChanged();
  }

  @Override
  public void taskCompleted(final Task task) {
    // no op
  }

  @Override
  public void taskChanged(final Task task) {
    blockEntity.setChanged();
  }
}
