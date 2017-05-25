package cn.ycoder.android.library.multitype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.multitype.ItemViewBinder;


/**
 * @author 启研
 * @created at 2017/4/11 16:38
 */

public abstract class CommonViewProvider<E> extends ItemViewBinder<E, RecyclerView.ViewHolder> {

  Context mContext;
  Map<E, View> sparseArray = new HashMap<>();

  /**
   * 实例化泛型
   */
  public abstract View instantingGenerics();

  @NonNull
  @Override
  protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
      @NonNull ViewGroup parent) {
    mContext = inflater.getContext();
    final RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(instantingGenerics()) {
    };
    if (clickListener != null) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          clickListener.onItemClick(holder.itemView);
        }
      });
    }
    if (longClickListener != null) {
      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          longClickListener.onItemLongClick(holder.itemView);
          return false;
        }
      });
    }
    return holder;
  }

  @Override
  protected void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @NonNull E t) {
    if (holder.itemView instanceof RecyclerViewControl) {
      ((RecyclerViewControl) holder.itemView).setValue(holder.getAdapterPosition(), t);
    }
  }

  public Context getContext() {
    return mContext;
  }

  public interface RecyclerViewControl<E> {

    /**
     * 给控件设值
     */
    void setValue(int postion, E e);

    E getValue();
  }


  private OnItemLongClickListener longClickListener;

  public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
    this.longClickListener = longClickListener;
  }

  private OnItemClickListener clickListener;

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.clickListener = listener;
  }

  public interface OnItemClickListener {

    void onItemClick(View view);
  }

  public interface OnItemLongClickListener {

    boolean onItemLongClick(View view);
  }
}
