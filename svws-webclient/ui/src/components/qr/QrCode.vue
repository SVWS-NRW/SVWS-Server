<template>
	<div class="qr-container">
		<div v-if="d !== null" class="qr-wrapper">
			<svg :viewBox="`0 0 ${qr.size} ${qr.size}`" shape-rendering="crispEdges">
				<path :d fill="currentColor" />
			</svg>
		</div>
		<div v-else class="error">
			Fehler beim Generieren des QR-Codes.
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';
	import { QrCode, Ecc } from './qrcode';

	const props = defineProps<{
		uri: string | URL;
	}>();

	const qr = computed(() => QrCode.encodeText(props.uri.toString(), Ecc.MEDIUM));
	const d = computed(() => {
		try {
			let parts = "";
			for (let y = 0; y < qr.value.size; y++) {
				for (let x = 0; x < qr.value.size; x++) {
					if (qr.value.getModule(x, y)) {
						parts += `M${x},${y}h1v1h-1z `;
					}
				}
			}
			return parts;
		} catch {
			return null;
		}
	});

</script>

<style scoped>
.qr-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.qr-wrapper {
  width: 100%;
  max-width: 250px; /* Größe nach Bedarf anpassen */
  aspect-ratio: 1 / 1;
  /* "Quiet Zone": QR-Codes brauchen einen hellen Rand zum Scannen */
  padding: 20px;
  background: white;
}

svg {
  display: block;
  width: 100%;
  height: 100%;
  color: black; /* Bestimmt die Farbe des 'currentColor' im Path */
}

.error {
  color: red;
  font-size: 0.9rem;
}
</style>