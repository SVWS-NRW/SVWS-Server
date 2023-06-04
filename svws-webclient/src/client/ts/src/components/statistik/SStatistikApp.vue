<template>
	<div v-if="data" class="page--flex page--statistik">
		<svws-ui-header>
			<div class="flex items-center">
				<div>
					<span class="inline-block mr-3"> Schulname </span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="[]" :hidden="[]" :model-value="undefined" @update:model-value="[]">
			<router-view />
			<div class="page--content">
				<svws-ui-content-card>
					<ul class="statistik--dashboard">
						<li class="statistik--dashboard--item">
							<span class="dashboard--title" title="Schüler*innen">Schüler*innen</span>
							<span class="dashboard--number">1.567</span>
						</li>
						<li class="statistik--dashboard--item">
							<span class="dashboard--title" title="Lehrkräfte">Lehrkräfte</span>
							<span class="dashboard--number">108</span>
						</li>
						<li class="statistik--dashboard--item">
							<span class="dashboard--title" title="Lehrkräfte">Kurse</span>
							<span class="dashboard--number">40</span>
						</li>
					</ul>
					<div class="mt-9 flex gap-3">
						<svws-ui-button>Button</svws-ui-button>
						<svws-ui-button type="secondary">Button</svws-ui-button>
						<svws-ui-button type="transparent">Button</svws-ui-button>
						<svws-ui-button type="icon"><i-ri-alert-line /></svws-ui-button>
					</div>
				</svws-ui-content-card>
			</div>
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<i-ri-bar-chart-2-line />
	</div>
</template>

<script setup lang="ts">
	const data = true;
</script>

<style lang="postcss" scoped>
.statistik--dashboard {
	@apply grid grid-cols-2 sm:grid-cols-4 gap-3;
}

.statistik--dashboard--item {
	@apply flex flex-col bg-purple-50 rounded-lg p-5;

	.dashboard--title {
		@apply line-clamp-1;
	}

	.dashboard--number {
		@apply text-headline-xl text-violet-500;
	}
}
</style>
