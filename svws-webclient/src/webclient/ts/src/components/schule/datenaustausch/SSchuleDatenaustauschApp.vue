<template>
	<div>
		<svws-ui-header>Datenaustausch</svws-ui-header>
		<svws-ui-tab-bar>
			<template #tabs>
				<svws-ui-tab-button>Laufbahnplanung</svws-ui-tab-button>
			</template>
			<template #panels>
				<svws-ui-tab-panel>
					<s-schule-datenaustausch-laufbahnplanung :set-gost-lupo-import-m-d-b-fuer-jahrgang="setGostLupoImportMDBFuerJahrgang" />
				</svws-ui-tab-panel>
			</template>
		</svws-ui-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import { SchuleDatenaustauschAppProps } from './SSchuleDatenaustauschAppProps';

	const props = defineProps<SchuleDatenaustauschAppProps>();
</script>
