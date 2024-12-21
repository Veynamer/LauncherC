import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class BatteryLevelView extends View {

    private Paint paint;
    private Path path;
    private int level = 50; // Уровень заряда (0-100)

    public BatteryLevelView(Context context) {
        super(context);
        init();
    }

    public BatteryLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE); // Цвет "жидкости"
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true); // Сглаживание

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Расчет высоты уровня "жидкости"
        float levelHeight = (float) level / 100 * height;

        // Отрисовка прямоугольника, имитирующего уровень жидкости
        path.reset();
        path.moveTo(0, height);
        path.lineTo(0, height - levelHeight);
        // Добавим небольшую волну (упрощенно)
        path.quadTo(width / 4f, height - levelHeight - 10, width / 2f, height - levelHeight);
        path.quadTo(width * 3/4f, height - levelHeight + 10, width, height - levelHeight);
        path.lineTo(width, height);
        path.close();

        canvas.drawPath(path, paint);

        // Отрисовка контура (опционально)
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, width, height, paint);
    }

    public void setLevel(int level) {
        this.level = level;
        invalidate(); // Перерисовываем виджет
    }
}
