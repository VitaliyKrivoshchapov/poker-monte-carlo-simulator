# Poker Monte Carlo Simulator  

A Java-based simulation of Texas Hold'em poker hands using the Monte Carlo method.  
Compares performance of:  
- **Single-threaded** execution  
- **Multi-threaded** (FixedThreadPool)  
- **Virtual Threads** (Java 21+)  

## 📊 Results  
For 1,000,000 iterations with hand `[Ace♠, King♠]`:  

| Runner           | Win Rate | Time (ms) | Memory Usage | Threads |  
|------------------|----------|-----------|--------------|---------|  
| SingleThread     | 66.70%   | 65,290    | 191/512 MB   | 1       |  
| MultiThread (8)  | 66.67%   | 11,395    | 67/740 MB    | 16      |  
| VirtualThread (8)| 66.75%   | 11,359    | 495/888 MB   | 24      |  

**Key Takeaways**:  
- MultiThread and VirtualThread are **~5.7x faster** than SingleThread.  
- VirtualThreads use **more memory** but simplify concurrency code.  


   ./mvnw clean package   # if using Maven
   java -jar target/poker-simulator.jar
