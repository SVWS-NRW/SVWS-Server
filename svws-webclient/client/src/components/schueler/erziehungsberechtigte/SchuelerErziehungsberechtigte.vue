<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-schueler-erziehungsberechtigte /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<schueler-erziehungsberechtigte-wrapper :data :patch-erzieher :patch-erzieher-an-position :add-erzieher :delete-erzieher />
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerErziehungsberechtigteProps } from "./SchuelerErziehungsberechtigteProps";

	const props = defineProps<SchuelerErziehungsberechtigteProps>();

</script>
