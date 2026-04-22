#!/bin/bash
# Jenkins Webhook Auto-Updater
# Run this script whenever you want to start your Jenkins CI pipeline.
# It starts ngrok, grabs the public URL, and prints the webhook URL to paste into GitHub.

echo "Starting ngrok tunnel to Jenkins on port 8080..."
ngrok http 8080 &
NGROK_PID=$!

# Wait for ngrok to start
sleep 3

# Get the public URL from ngrok's local API
NGROK_URL=$(curl -s http://127.0.0.1:4040/api/tunnels | python3 -c "
import sys, json
data = json.load(sys.stdin)
for t in data['tunnels']:
    if t['proto'] == 'https':
        print(t['public_url'])
        break
")

if [ -z "$NGROK_URL" ]; then
    echo "Could not get ngrok URL. Make sure ngrok is installed."
    exit 1
fi

WEBHOOK_URL="${NGROK_URL}/github-webhook/"

echo ""
echo "=========================================="
echo "Jenkins is now publicly accessible!"
echo "=========================================="
echo ""
echo "Your webhook URL:"
echo "$WEBHOOK_URL"
echo ""
echo "Go to:"
echo "  github.com/Rmark8448/jenkinsWorkshop -> Settings -> Webhooks"
echo "  Update the Payload URL to: $WEBHOOK_URL"
echo ""
echo "Press Ctrl+C to stop ngrok when done."
echo ""

wait $NGROK_PID
