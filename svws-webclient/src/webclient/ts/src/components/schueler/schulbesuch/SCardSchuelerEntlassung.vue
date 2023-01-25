<template>
	<svws-ui-content-card title="Entlassung von eigener Schule">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Entlassung am" v-model="entlassungDatum" type="date" />
			<svws-ui-text-input placeholder="Entlassjahrgang" v-model="entlassungJahrgang" type="date" />
			<svws-ui-text-input placeholder="Bemerkung / Entlassgrund" v-model="entlassungGrundID" />
			<svws-ui-text-input placeholder="Art des Abschlusses" v-model="entlassungAbschlussartID" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { PropDataSchulbesuch } from "./PropDataSchulbesuch";

	const props = defineProps<{
		data: PropDataSchulbesuch;
	}>();

	const entlassungDatum = props.data.entlassungDatum();
	const entlassungJahrgang = props.data.entlassungJahrgang();
	const entlassungGrundID = props.data.entlassungGrundID();
	const entlassungAbschlussartID = props.data.entlassungAbschlussartID();

</script>
