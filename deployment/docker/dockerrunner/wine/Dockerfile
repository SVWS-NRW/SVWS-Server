FROM 10.1.1.3/svwsnrw/docker-runner:21

ENV DEBIAN_FRONTEND=noninteractive

RUN <<EOF
apt-get update
apt-get -y upgrade
apt-get -y install wget gnupg unzip
dpkg --add-architecture i386
mkdir -p /etc/apt/keyrings
wget -O /etc/apt/keyrings/winehq-archive.key https://dl.winehq.org/wine-builds/winehq.key
wget -NP /etc/apt/sources.list.d/ https://dl.winehq.org/wine-builds/ubuntu/dists/noble/winehq-noble.sources
apt update
apt install --install-recommends winehq-stable -y
EOF