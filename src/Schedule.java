import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Schedule
{
   private ArrayList<Event> events;
   private ArrayList<RecurringEvent> recurringEvents;
   private Calendar weekStart;
   private Calendar weekEnd;
   
   public Schedule()
   {
      events = new ArrayList<Event>();
      recurringEvents = new ArrayList<>();
      
      weekStart = Calendar.getInstance();
      weekStart.add(Calendar.DAY_OF_MONTH, -weekStart.get(Calendar.DAY_OF_WEEK) + 1);
      weekStart.add(Calendar.HOUR_OF_DAY, -weekStart.get(Calendar.HOUR_OF_DAY));
      weekStart.add(Calendar.MINUTE, -weekStart.get(Calendar.MINUTE));
      weekEnd = (Calendar) weekStart.clone();
      weekEnd.add(Calendar.DAY_OF_MONTH, 6);
   }

   public Calendar getWeekStart()
   {
       return weekStart;
   }

   public Calendar getWeekEnd()
   {
       return weekEnd;
   }
   
   public boolean addEvent(Event e)
   {
      boolean add = true;
      int i = 0;
      boolean c = true;
      while(c && i < events.size())
      {
         if(!events.get(i).getEndTime().after(e.getStartTime()))
            i++;
         else
            if(e.getEndTime().after(events.get(i).getStartTime()))
            {
               add = false;
               c = false;
            }
            else
               c = false;
      }
      if(add)
         events.add(i,e);
      return add;
   }

   public boolean addRecurringEvent(RecurringEvent re)
   {
      recurringEvents.add(re);
      return true;
   }

   public ArrayList<Event> getAllEvents()
   {
      return events;
   }

   public ArrayList<Event> getEvents(Calendar start, Calendar end)
   {
       ArrayList<Event> events = (ArrayList<Event>) this.events.clone();
       for(int i = events.size() - 1; i >= 0; i--)
       {
          if(!events.get(i).between(start, end))
          {
             events.remove(i);
          }
       }
       for(RecurringEvent re : recurringEvents)
       {
          events.addAll(re.getEventsInSpan(start, end));
       }
       return events;
   }
   
   public ArrayList<Event> getEmptyTimes(Calendar start, Calendar end)
   {
      ArrayList<Event> empty = new ArrayList<Event>();
      int i = 0;
      while(events.get(i).getEndTime().before(start))
         i++;
      if(events.get(i).getStartTime().after(start))
         empty.add(new Event(start,events.get(i).getStartTime(),""));
      while(i+1 < events.size() && events.get(i+1).getStartTime().before(end))
      {
         empty.add(new Event(events.get(i).getEndTime(),events.get(i+1).getStartTime(),""));
         i++;
      }
      if(events.get(i).getEndTime().before(end))
         empty.add(new Event(events.get(i).getEndTime(),end,""));
      return empty;
      
   }

   public String getWeekStartString()
   {
      String date = "";
      date += weekStart.get(Calendar.MONTH) + 1;
      date += "/";
      date += weekStart.get(Calendar.DAY_OF_MONTH);
      date += "/";
      date += weekStart.get(Calendar.YEAR);
      return date;
   }
   
   public String getWeekEndString()
   {
      String date = "";
      date += weekEnd.get(Calendar.MONTH) + 1;
      date += "/";
      date += weekEnd.get(Calendar.DAY_OF_MONTH);
      date += "/";
      date += weekEnd.get(Calendar.YEAR);
      return date;
   }

   public void advanceOneWeek()
   {
      weekStart.add(Calendar.DAY_OF_MONTH, 7);
      weekEnd.add(Calendar.DAY_OF_MONTH, 7);
   }

   public void goBackOneWeek()
   {
      weekStart.add(Calendar.DAY_OF_MONTH, -7);
      weekEnd.add(Calendar.DAY_OF_MONTH, -7);
   }

   public String fileString()
   {
      String s = "";
      for(int i = 0; i < events.size(); i++)
      {
         s+=events.get(i).fileString();
         if(i+1<events.size())
            s+="\n";
      }
      return s;
   }

   public String toString()
   {
      String s = "";
      for(int i = 0; i < events.size(); i++)
         s = s+events.get(i)+"\n";
      return s;
   }
   
}
