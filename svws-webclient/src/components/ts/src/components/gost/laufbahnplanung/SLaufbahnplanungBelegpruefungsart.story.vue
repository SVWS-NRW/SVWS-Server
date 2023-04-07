<template>
	<Story title="GostBelegpruefungsarten">
		<Variant title="GostBelegpruefungsArt">
			<s-laufbahnplanung-belegpruefungsart v-model="art" />
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { GostBelegpruefungsArt } from '@svws-nrw/svws-core';
	import { shallowRef } from 'vue';

	const art = shallowRef(GostBelegpruefungsArt.GESAMT)
</script>
