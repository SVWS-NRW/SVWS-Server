<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-header>
				<span>{{
					schule.daten?.bezeichnung1
				}}</span>
			</svws-ui-header>
			<svws-ui-tab-bar>
				<template #tabs>
					<svws-ui-tab-button>Daten</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
						<s-schule-datenaustausch-app />
					</svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { DataSchuleStammdaten } from '~/apps/schule/DataSchuleStammdaten';

	const props = defineProps<{
		schule: DataSchuleStammdaten;
	}>();

</script>
